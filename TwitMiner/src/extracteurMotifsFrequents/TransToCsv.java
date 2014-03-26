package extracteurMotifsFrequents;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransToCsv {
	String ligne = "";
	List<String> table = new ArrayList<>();
	String m_nomFichier = "";

	public TransToCsv(String nomFichier) throws IOException {
		this.m_nomFichier = nomFichier;
	}

	public void run() throws IOException {

		try {
			BufferedReader fichierTrans = new BufferedReader(new FileReader(
					m_nomFichier));
			File result = new File("Apriori\\src\\result.csv");
			if (!result.exists()) {
				result.createNewFile();
			}
			BufferedWriter fichierCSV = new BufferedWriter(new FileWriter(
					result));
			BufferedReader fichierCorrespondance = new BufferedReader(
					new FileReader("Apriori\\src\\correspondance.txt"));

			while ((ligne = fichierCorrespondance.readLine()) != null) {
				table.add(ligne);
				// On écrit la table de correspondance dans table
			}

			while ((ligne = fichierTrans.readLine()) != null) {

				String ecriture = "\"";
				String[] transactions = ligne.toLowerCase().split(" ");

				for (int i = 0; i < transactions.length - 1; ++i) {
					ecriture += table.get(Integer.parseInt(transactions[i]))
							+ "\";\"";
				}
				// On ajoute le mot correspondant et un séparateur
				System.out.println(ecriture);
				fichierCSV.write(ecriture);
				// fichierCSV.newLine();

			}

			fichierTrans.close();
			fichierCSV.close();
			fichierCorrespondance.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}