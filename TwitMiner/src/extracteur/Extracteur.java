package extracteur;

import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class Extracteur extends Thread {
	Twitter twitter;
	String motCle;
	long NbTweetsARecuperer;

	// Fonction qui filtre les tweets : suppression des retours à la ligne, des
	// "..." à la fin etc
	public String filtrer(String msg) {
		String resultat = msg.toString();
		resultat = resultat.replace("\n", " ");
		return resultat;
	}

	public Extracteur(String motCle, long NbTweetsARecuperer, Twitter twitter) {
		this.motCle = motCle.toLowerCase(); // L'extracteur n'est pas sensible à
											// la casse
		this.twitter = twitter;
		this.NbTweetsARecuperer = NbTweetsARecuperer;
	}

	public void run() {
		// Requête
		// Les tweets récupérés contiendront ce qui sera passé en argument à la
		// ligne suivante :
		try {
			// Ce n'est qu'un indice de parcours de boucle, à ne pas changer

			Query query = new Query(motCle);
			QueryResult result;
			long NbTweetsEnregistres = 0;
			if (NbTweetsARecuperer > 0) {
				do {
					result = twitter.search(query);
					List<Status> tweets = result.getTweets();
					for (Status tweet : tweets) {
						if (!tweet.isRetweet()
								&& tweet.getText().toLowerCase()
										.matches(".*\\b" + motCle + "\\b.*")) {
							System.out.println("@"
									+ tweet.getUser().getScreenName() + " - "
									+ filtrer(tweet.getText()));
							++NbTweetsEnregistres;
						}
						// System.out.println("Nb de tweets récupérés : "
						// + NbTweetsEnregistres);
						if (NbTweetsEnregistres >= NbTweetsARecuperer)
							break;
					}

				} while (NbTweetsEnregistres < NbTweetsARecuperer
						&& (query = result.nextQuery()) != null);
			}

			/*
			 * List<twitter4j.Status> tweets = result.getTweets(); do { for
			 * (twitter4j.Status tweet : tweets) { if ((query =
			 * result.nextQuery()) == null || NbTweets >= TweetsARecuperer)
			 * break; // On veut que le motcle soit contenu dans le tweet, et
			 * pas // dans le pseudo uniquement. La requête de twitter4j //
			 * cherche le mot-clé dans les status, mais aussi dans les //
			 * pseudos. // De plus, on enlève les retweets if
			 * (!tweet.isRetweet() && tweet.getText().toLowerCase()
			 * .matches(".*\\b" + motCle + "\\b.*")) { // Ligne suivante à
			 * remplacer par l'écriture du tweet // dans // un fichier .csv
			 * 
			 * System.out.println(tweet.getId() + " " +
			 * tweet.getIsoLanguageCode() + " " + tweet.getRetweetCount() + " "
			 * + tweet.getCreatedAt() + " @" + tweet.getUser().getScreenName() +
			 * " - " + tweet.getText()); ++NbTweets;
			 * 
			 * } System.out.println("Nb de tweets récupérés : " + NbTweets);
			 * 
			 * }
			 * 
			 * } while (NbTweets < TweetsARecuperer); // Oui cette boucle est
			 * illisible
			 */

		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// run()
}// Extracteur
