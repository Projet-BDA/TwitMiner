import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class CSVToTrans {

    String separateur = "\";\"";
    String ligne = "";
    List<String> table = new ArrayList<>();
    String m_nomFichier = "";

    public CSVToTrans(String nomFichier) throws IOException {
        this.m_nomFichier = nomFichier;
    }

    public void run() throws IOException {

        try {
            BufferedReader fichierCsv = new BufferedReader(new FileReader(m_nomFichier));
            File result = new File("Apriori\\src\\result.trans");
            if (!result.exists()) {
                result.createNewFile();
            }
            File correspondance = new File("Apriori\\src\\correspondance.txt");
            if (!correspondance.exists()) {
                correspondance.createNewFile();
            }
            BufferedWriter fichierTrans = new BufferedWriter(new FileWriter(result));
            BufferedWriter fichierCorrespondance = new BufferedWriter(new FileWriter(correspondance));

            while ((ligne = fichierCsv.readLine()) != null) {
                // Traitement d'un tweet

                String ecriture = "";
                String[] transactions = ligne.toLowerCase().split(separateur);

                // String[0] = id du tweet
                // String[1] = date
                // String[2] = pays
                // String[3] = auteur
                // Si tu veux rajouter un traitement

                for (int i = 4; i < transactions.length; ++i) {
                    // Traitement d'un mot
                    if (!table.contains(transactions[i])) {
                        // Si le mot apparait pour la première fois, on l'ajoute dans le tableau
                        table.add(transactions[i]);
                        fichierCorrespondance.write(transactions[i]);
                        fichierCorrespondance.newLine();
                    }
                    // On ajoute l'index lié au mot
                    ecriture += table.indexOf(transactions[i]) + " ";
                }
                //System.out.println(ecriture);
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