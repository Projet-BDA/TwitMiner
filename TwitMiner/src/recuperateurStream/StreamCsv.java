package recuperateurStream;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;

public class StreamCsv extends Thread {
	TwitterStream ts;
	String motsCles[];
	String nomFichier = new String("");
	StatusListener listener;
	BufferedWriter fichierTweets;
	int NbTweetsEnregistres = 0;
	int NbTweetsARecuperer = 0;
	Object lock = new Object();

	// Fonction qui filtre les tweets : suppression des retours à la ligne, des
	// "..." à la fin etc
	public String filtrer(String msg) {
		String resultat = msg.toString();
		resultat = resultat.replace("\n", " ");
		return resultat;
	}

	public StreamCsv(String motsCles[], final int NbTweetsARecuperer,
			TwitterStream ts) throws IOException {
		for (int i = 0; i < motsCles.length; ++i) {
			// L'extracteur n'est pas sensible à la casse NI AUX ACCENTS
			motsCles[i] = Normalizer.normalize(motsCles[i].toLowerCase(),
					Normalizer.Form.NFD);
		}

		this.motsCles = motsCles;
		this.ts = ts;
		this.NbTweetsARecuperer = NbTweetsARecuperer;

		// On va enregistrer les tweets dans un fichier
		DateFormat dateFormat = new SimpleDateFormat("HH_mm_ss");
		Date dateActuelle = new Date();
		for (int i = 0; i < motsCles.length; ++i) {
			this.nomFichier += motsCles[i] + "-";
		}
		this.nomFichier = this.nomFichier.replaceAll("[+.^:,?]", "")
				+ dateFormat.format(dateActuelle) + ".csv";
		fichierTweets = new BufferedWriter(new FileWriter(new File(nomFichier),
				true));
		// Ce qui donnera par exemple, avec les mots clés amour et love
		// amour-love-12_03_47.csv

		listener = new StatusListener() {

			@Override
			public void onException(Exception arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStatus(Status status) {
				// On met en forme le tweet, comme dans l'autre récupérateur
				// (pas de retweet)
				if (!status.isRetweet()) {
					String ligne = ("\"" + status.getId() + "\";\""
							+ status.getCreatedAt() + "\";\""
							+ status.getLang() + "\";\"@" + status.getUser()
							.getScreenName());
					// Séparation de chaque mot par un ;
					String[] motsDuTweet = filtrer(status.getText()).split(" ");
					for (int i = 0; i < motsDuTweet.length; ++i) {
						ligne += "\";\"" + motsDuTweet[i];
					}
					ligne += "\";\"";
					// On l'affiche dans la console...
					System.out.println(ligne);

					try {
						fichierTweets.write(ligne + "\"\n\"");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// Voir la RFC d'un fichier .csv. Chaque ligne
					// du classeur est séparée par un retour à la
					// ligne qui DOIT être compris entre des double
					// quotes
					++NbTweetsEnregistres;
					if (NbTweetsEnregistres >= NbTweetsARecuperer){
						// On a le nombre de tweets qu'il faut, on le signale pour que le stream soit arrêté
						synchronized (lock) {
							lock.notify();
						}
					}
				}
				System.out.println("Nb de tweets récupérés : "
						+ NbTweetsEnregistres);

			}

			@Override
			public void onTrackLimitationNotice(int arg0) {
				System.out.println("Nombre de tweets demandés : " + arg0);

			}

			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub

			}

		};
	}

	public String getNomFichier() {
		return nomFichier;
	}

	public void run() {
		FilterQuery fq = new FilterQuery();
		// On recherche dans le flux tous les mots passés en arguments dans
		// le tableau !
		fq.track(motsCles);

		ts.addListener(listener);
		listener.onTrackLimitationNotice(NbTweetsARecuperer);
		// Et là on "consomme" les mots recherchés ! Facile non ? (Ok j'ai
		// pas tout compris)

		ts.filter(fq);
		synchronized(lock){
			try {
				// On attend que le stream ait fini de récupérer le nombre de tweets demandés
				lock.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Terminé !");
		try {
			fichierTweets.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ts.shutdown();
		try {
			fichierTweets.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}// run()
}// Extracteur
