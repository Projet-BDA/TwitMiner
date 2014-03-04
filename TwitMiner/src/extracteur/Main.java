// NOTE : si problème d'encodage ( tweets avec des "?????"),
// dans eclipse : Window -> Preferences -> General ->
// Workspace : Text file encoding : UTF-8

package extracteur;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class Main {

	public static void main(String[] args) throws TwitterException {
		// Configuration avec l'application twitter
		TwitterFactory tf = new TwitterFactory();
		Twitter twitter = tf.getInstance();
		
		// Extracteur extracteur_love = new Extracteur("love", 10, twitter);
		Extracteur extracteur_amour = new Extracteur("amour", 50, twitter);
		// extracteur_love.start();
		extracteur_amour.start();

	}

}
