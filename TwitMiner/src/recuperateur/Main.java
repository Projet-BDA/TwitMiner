// NOTE : ATTENTION A L'ENCODAGE !!! si problème d'encodage ( tweets avec des "?????"),
// dans eclipse : Window -> Preferences -> General ->
// Workspace : Text file encoding : UTF-8

package recuperateur;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class Main {

	public static void main(String[] args) throws TwitterException,
			InterruptedException {

		// Configuration avec l'application twitter
		TwitterFactory tf = new TwitterFactory();
		Twitter twitter = tf.getInstance();

		RecuperateurCsv extracteur;

		extracteur = new RecuperateurCsv(args[0], Integer.parseInt(args[1]),
				twitter);
		if (args.length == 3)
		// Le 3ème argument sert à rajouter l'id maximum des tweets, pour
		// reprendre une requête à un point précis.
		{
			extracteur = new RecuperateurCsv(args[0], Integer.parseInt(args[1]),
					twitter, Long.parseLong(args[2]));
		} else if (args.length != 2) {
			System.out
					.println("ERREUR : Il faut 2 arguments : le mot-clé et le nombre de tweets désirés. Exemple : twitminer.jar bonjour 50");
		}

		extracteur.start();
		extracteur.join();
		System.out
				.println("Phase d'extraction terminée. Consultez les tweets dans : "
						+ extracteur.getNomFichier());
	}

}
