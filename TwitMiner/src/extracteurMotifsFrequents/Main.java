package extracteurMotifsFrequents;

import java.io.IOException;
import java.util.Scanner;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Usage : xxx.jar nomdufichier");
		} else {
			String nomFichier = args[0];
			System.out.println(nomFichier);
			int typeConversion = 0;
			Scanner sc = new Scanner(System.in);
			while (typeConversion != 1 && typeConversion != 2) {
				System.out
						.println("Comment souhaitez-vous convertir votre fichier ?"
								+ "\n1) csv -> trans" + "\n2) trans -> csv");

				typeConversion = sc.nextInt();

				// System.out.println(typeConversion);
			}
			sc.close();
			if (typeConversion == 1) {
				CsvToTrans ct = new CsvToTrans(nomFichier);
				ct.run();
			} else if (typeConversion == 2) {
				TransToCsv tc = new TransToCsv(nomFichier);
				tc.run();
			}

		}
	}
}
