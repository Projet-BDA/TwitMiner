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

	public Extracteur(String motCle, ConfigurationBuilder cb, Twitter twitter) {
		this.motCle = motCle;
		this.cb = cb;
		this.twitter = twitter;
	}

	public void run() {
		// Requête
		// Les tweets récupérés contiendront ce qui sera passé en argument à la
		// ligne suivante :
		try {
			Query query = new Query(motCle);
			QueryResult result;
			// Ce n'est qu'un indice de parcours de boucle, à ne pas changer
			int NbTweets = 0;
			// Changez ici le nombre de tweets à récupérer
			int NbMaxTweets = 10;
			do {

				result = twitter.search(query);

				List<twitter4j.Status> tweets = result.getTweets();
				for (twitter4j.Status tweet : tweets) {
					System.out.println(" " + tweet.getIsoLanguageCode() + " "
							+ tweet.getRetweetCount() + " "
							+ tweet.getCreatedAt() + " @"
							+ tweet.getUser().getScreenName() + " - "
							+ tweet.getText());
					++NbTweets;
					;
					if (NbTweets >= NbMaxTweets)
						break;
				}

			} while (NbTweets <= NbMaxTweets
					&& (query = result.nextQuery()) != null);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// run()

}// Extracteur
