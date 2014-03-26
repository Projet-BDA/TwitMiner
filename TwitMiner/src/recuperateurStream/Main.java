// NOTE : ATTENTION A L'ENCODAGE !!! si problème d'encodage ( tweets avec des "?????"),
// dans eclipse : Window -> Preferences -> General ->
// Workspace : Text file encoding : UTF-8

package recuperateurStream;

import java.io.IOException;

import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class Main {

	public static void main(String[] args) throws TwitterException,
			InterruptedException, IOException {
		if (args.length <= 1) {
			System.out
					.println("Usage : xxx.jar motCle1 motCle2 ... motCleN NombreDeTweets");
		} else {
			// Configuration avec l'application twitter
			TwitterStream ts = new TwitterStreamFactory().getInstance();

			// Utilisation : xxx.jar motCle1 motCle2 ... motCleN NombreDeTweets

			String[] motsCles = new String[args.length - 1];
			// On rempli le tableau de mots clés avec ce qui est passé en
			// arguments
			for (int i = 0; i < args.length - 1; ++i) {
				motsCles[i] = args[i];
			}

			int NbTweetsARecuperer = Integer.parseInt(args[args.length - 1]);

			StreamCsv extracteur;

			extracteur = new StreamCsv(motsCles, NbTweetsARecuperer, ts);

			extracteur.start();
			extracteur.join();
			System.out
					.println("Phase d'extraction terminée. Consultez les tweets dans : "
							+ extracteur.getNomFichier());

		}
	}

}
