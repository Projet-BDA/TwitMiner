// Hors sujet : crée un fichier txt (lisible faiclement à l'aide d'un éditeur de texte) au lieu d'un fichier csv

package recuperateur;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class RecuperateurTxt extends Thread {
	Twitter twitter;
	String motCle;
	long NbTweetsARecuperer;
	String nomFichier;

	// Fonction qui filtre les tweets : suppression des retours à la ligne, des
	// "..." à la fin etc
	public String filtrer(String msg) {
		String resultat = msg.toString();
		resultat = resultat.replace("\n", " ");
		return resultat;
	}

	public RecuperateurTxt(String motCle, long NbTweetsARecuperer, Twitter twitter) {
		this.motCle = Normalizer.normalize(motCle.toLowerCase(),
				Normalizer.Form.NFD); // L'extracteur n'est pas sensible à
		// la casse NI AUX ACCENTS
		this.twitter = twitter;
		this.NbTweetsARecuperer = NbTweetsARecuperer;
	}

	public String getNomFichier() {
		return nomFichier;
	}

	public void run() {
		// Requête
		// Les tweets récupérés contiendront ce qui sera passé en argument à la
		// ligne suivante :
		try {
			// On enregistre les tweets dans un fichier
			DateFormat dateFormat = new SimpleDateFormat(" - HH_mm_ss");
			Date dateActuelle = new Date();
			this.nomFichier = motCle.replaceAll("[-+.^:,?]", "")
					+ dateFormat.format(dateActuelle) + ".txt";
			File fichier = new File(nomFichier);
			@SuppressWarnings("resource")
			BufferedWriter fichierTweets = new BufferedWriter(new FileWriter(
					fichier, true));
			Query query = new Query(motCle);
			QueryResult result;
			long NbTweetsEnregistres = 0;
			long idLePlusAncienRecupere = 0;
			if (NbTweetsARecuperer > 0) {
				do {
					try {
						// 100 tweets par page (le max)
						result = twitter.search(query.count(100));
						// Récupération des tweets dans une liste
						List<Status> tweets = result.getTweets();
						for (Status tweet : tweets) {
							// Ici on analyse les tweets. Est-ce un nouveau
							// tweet ? Et est-ce que le mot-clé recherché est
							// bien dans le message ou le pseudo (auquel cas on
							// ignore le tweet) ? Est un retweet (si oui on
							// jette aussi) ?
							if (tweet.getId() != idLePlusAncienRecupere
									&& !tweet.isRetweet()
									&& Normalizer.normalize(
											tweet.getText().toLowerCase(),
											Normalizer.Form.NFD).matches(
											".*\\b" + motCle + "\\b.*")) {
								// Mise en forme des tweets
								// (" id - date - fr @pseudo blablabla")
								String ligne = (tweet.getId() + " - "
										+ tweet.getCreatedAt() + " "
										+ tweet.getLang() + " @"
										+ tweet.getUser().getScreenName()
										+ " - " + filtrer(tweet.getText()));
								// On l'affiche dans la console...
								System.out.println(ligne);
								// Puis on l'insère dans le fichier ouvert au
								// début
								fichierTweets.write(ligne
										+ System.getProperty("line.separator"));
								++NbTweetsEnregistres;
								// On garde l'id du dernier tweet pour éviter
								// les doublons (répond à la question
								// "Est-ce un nouveau tweet ?")
								idLePlusAncienRecupere = tweet.getId();

							}

							System.out.println("Nb de tweets récupérés : "
									+ NbTweetsEnregistres);
							if (NbTweetsEnregistres >= NbTweetsARecuperer)
								// Le compte est bon
								break;
						}
						result = twitter.search(query
								.maxId(idLePlusAncienRecupere));
					} catch (TwitterException e) {
						if (e.exceededRateLimitation()) {
							// Trop de requêtes, il faut attendre.
							int nbMinutes = 1;
							sleep(nbMinutes * 60 * 1000);
							System.out
									.println("Nombre de reqûetes autorisées dépassé. Nouvel essai dans "
											+ nbMinutes + " minute(s)...");
						} else
							// Si l'erreur vient d'ailleurs, on fait remonter
							// l'exception
							throw e;
					}
				} while (NbTweetsEnregistres < NbTweetsARecuperer);
			}

			// Fermeture du fichier, tous les tweets ont normalement été
			// récupérés
			fichierTweets.close();

		} catch (TwitterException | IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}// run()
}// Extracteur
