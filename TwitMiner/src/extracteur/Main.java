// NOTE : si problème d'encodage ( tweets avec des "?????"),
// dans eclipse : Window -> Preferences -> General ->
// Workspace : Text file encoding : UTF-8

package extracteur;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Main {

	public static void main(String[] args) throws TwitterException {
		// Configuration avec l'application twitter
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey("C6of4Nsh6pbdOaSWt7Kiw")
				.setOAuthConsumerSecret(
						"BTV7nKrI36kqaQ8Xm9O3P0kSXbnkI3nM2hfp06GB5Y")
				.setOAuthAccessToken(
						"993721920-dPgR6n7ggwNy4rY8Mxe4PTYDGsfVfLcUukiqcECH")
				.setOAuthAccessTokenSecret(
						"ZCXUiVvmPChcp4siwOi3sons812JQL8UfiwUT5vmcg2hK");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		// Extracteur extracteur_love = new Extracteur("love", 10, cb, twitter);
		Extracteur extracteur_amour = new Extracteur("amour", 10, cb, twitter);
		// extracteur_love.start();
		extracteur_amour.start();

	}

}
