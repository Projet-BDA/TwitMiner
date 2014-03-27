package extracteurMotifsFrequents;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class CsvToTrans {

	String separateur = "\";\"";
	List<String> table = new ArrayList<>();
	String nomFichier = "";

	public CsvToTrans(String nomFichier) throws IOException {
		this.nomFichier = nomFichier;
	}

	public void run() throws IOException {

		try {
			BufferedReader fichierCsv = new BufferedReader(new FileReader(
					nomFichier));
			File result = new File(nomFichier.substring(0,
					nomFichier.lastIndexOf('.'))
					+ ".trans");
			System.out.println(result.getName() + " en cours de génération...");
			if (!result.exists()) {
				result.createNewFile();
			}
			File correspondance = new File(nomFichier.substring(0,
					nomFichier.lastIndexOf('.'))
					+ ".transc");
			if (!correspondance.exists()) {
				correspondance.createNewFile();
			}
			BufferedWriter fichierTrans = new BufferedWriter(new FileWriter(
					result));
			BufferedWriter fichierCorrespondance = new BufferedWriter(
					new FileWriter(correspondance));
			String ligne = new String("gnééééé");
			while ((ligne = fichierCsv.readLine()) != null && ligne.length() >= 2) {
				// Traitement d'un tweet
				System.out.println(ligne);
				String ecriture = new String("");
				ligne = Normalizer.normalize(ligne.toLowerCase(),
						Normalizer.Form.NFD);
				String[] transactions = ligne.split(separateur);

				// String[0] = id du tweet
				// String[1] = date
				// String[2] = pays
				// String[3] = auteur
				// Si tu veux rajouter un traitement

				for (int i = 4; i < transactions.length; ++i) {
					// Traitement d'un mot
					if (!table.contains(transactions[i])) {
						// Si le mot apparait pour la première fois, on l'ajoute
						// dans le tableau
						table.add(transactions[i]);
						fichierCorrespondance.write(transactions[i]);
						fichierCorrespondance.newLine();
					}
					// On ajoute l'index lié au mot
					ecriture += table.indexOf(transactions[i]) + " ";
				}
				System.out.println(ecriture);
				fichierTrans.write(ecriture);
				fichierTrans.newLine();

			}

			fichierTrans.close();
			fichierCsv.close();
			fichierCorrespondance.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}