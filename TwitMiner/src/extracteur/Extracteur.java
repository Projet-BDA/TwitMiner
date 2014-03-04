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

	// Fonction qui filtre les tweets : suppression des retours à la ligne, des
	// "..." à la fin etc
	public String filtrer(twitter4j.Status msg) {
		String msgFiltre = msg.toString();
		return msgFiltre;
	}

	public Extracteur(String motCle, int TweetsARecuperer,
			ConfigurationBuilder cb, Twitter twitter) {
		this.motCle = motCle.toLowerCase(); // L'extracteur n'est pas sensible à
											// la casse
		this.cb = cb;
		this.twitter = twitter;
		this.TweetsARecuperer = TweetsARecuperer;
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
			do {

				result = twitter.search(query);

				List<twitter4j.Status> tweets = result.getTweets();
				for (twitter4j.Status tweet : tweets) {
					// On veut que le motcle soit contenu dans le tweet, et pas
					// dans le pseudo uniquement. La requête de twitter4j
					// cherche le mot-clé dans les status, mais aussi dans les
					// pseudos.
					// De plus, on enlève les retweets
					if (!tweet.isRetweet()
							&& tweet.getText().toLowerCase()
									.matches(".*\\b" + motCle + "\\b.*")) {
						// Ligne suivante à remplacer par l'écriture du tweet
						// dans
						// un fichier .csv

						System.out.println(tweet.getId() + " "
								+ tweet.getIsoLanguageCode() + " "
								+ tweet.getRetweetCount() + " "
								+ tweet.getCreatedAt() + " @"
								+ tweet.getUser().getScreenName() + " - "
								+ tweet.getText());
						++NbTweets;
					}
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
