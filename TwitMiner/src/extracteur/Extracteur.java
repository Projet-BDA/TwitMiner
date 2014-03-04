package extracteur;

import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.conf.ConfigurationBuilder;

public class Extracteur extends Thread {
	ConfigurationBuilder cb;
	Twitter twitter;
	String motCle;
	int TweetsARecuperer;

	public Extracteur(String motCle, int TweetsARecuperer, ConfigurationBuilder cb, Twitter twitter) {
		this.motCle = motCle;
		this.cb = cb;
		this.twitter = twitter;
		this.TweetsARecuperer = TweetsARecuperer;
	}

	public void run() {
		// Requ�te
		// Les tweets r�cup�r�s contiendront ce qui sera pass� en argument � la
		// ligne suivante :
		try {
			Query query = new Query(motCle);
			QueryResult result;
			// Ce n'est qu'un indice de parcours de boucle, � ne pas changer
			int NbTweets = 0;
			do {

				result = twitter.search(query);

				List<twitter4j.Status> tweets = result.getTweets();
				for (twitter4j.Status tweet : tweets) {
					// Ligne suivante à remplacer par l'écriture du tweet dans un fichier .csv
					// NOTE : si problème d'encodage ( tweets avec des "?????"), dans eclipse : Window -> Preferences -> General -> Workspace : Text file encoding : UTF-8
					System.out.println(" " + tweet.getIsoLanguageCode() + " "
							+ tweet.getRetweetCount() + " "
							+ tweet.getCreatedAt() + " @"
							+ tweet.getUser().getScreenName() + " - "
							+ tweet.getText());
					++NbTweets;
					;
					if (NbTweets >= TweetsARecuperer)
						break;
				}

			} while (NbTweets <= TweetsARecuperer
					&& (query = result.nextQuery()) != null);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// run()

}// Extracteur
