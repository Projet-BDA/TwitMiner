// NOTE : si problème d'encodage ( tweets avec des "?????"),
// dans eclipse : Window -> Preferences -> General ->
// Workspace : Text file encoding : UTF-8

package extracteur;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class Main {

	public static void main(String[] args) throws TwitterException, InterruptedException {
		if (args.length != 2) {
			System.out
					.println("ERREUR : Il faut 2 arguments : le mot-clé et le nombre de tweets désirés. Exemple : twitminer.jar bonjour 50");
		}
		// Configuration avec l'application twitter
		TwitterFactory tf = new TwitterFactory();
		Twitter twitter = tf.getInstance();


		Extracteur extracteur = new Extracteur(args[0],
				Integer.parseInt(args[1]), twitter);
		
		
		extracteur.start();
	}

}
