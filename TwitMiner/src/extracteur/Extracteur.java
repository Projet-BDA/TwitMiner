package extracteur;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
			// On enregistre les tweets dans un fichier
			DateFormat dateFormat = new SimpleDateFormat(" - HH_mm_ss");
			Date dateActuelle = new Date();
			File fichier = new File(motCle + dateFormat.format(dateActuelle) + ".txt");
			FileWriter fichierTweets = new FileWriter(fichier, true);

			Query query = new Query(motCle);
			QueryResult result;
			long NbTweetsEnregistres = 0;
            long idLePlusAncienRecupere = 0;
			if (NbTweetsARecuperer > 0) {
				do {
					result = twitter.search(query.count(100));
					List<Status> tweets = result.getTweets();
					for (Status tweet : tweets) {
						if (!tweet.isRetweet()
								&& tweet.getText().toLowerCase()
										.matches(".*\\b" + motCle + "\\b.*")) {
							String ligne = (tweet.getId() + " - " + tweet.getCreatedAt() + " "
									+ tweet.getLang() + " @"
									+ tweet.getUser().getScreenName() + " - " + filtrer(tweet
									.getText()));
							System.out.println(ligne);
							fichierTweets.write(ligne
									+ System.getProperty("line.separator"));
							++NbTweetsEnregistres;
							
						}
                        idLePlusAncienRecupere = tweet.getId();
                        result = twitter.search(query.maxId(idLePlusAncienRecupere));
						// System.out.println("Nb de tweets récupérés : "
						// + NbTweetsEnregistres);
						if (NbTweetsEnregistres >= NbTweetsARecuperer)
							break;
					}

				} while (NbTweetsEnregistres < NbTweetsARecuperer);
			}

			fichierTweets.close();

		} catch (TwitterException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// run()
}// Extracteur
