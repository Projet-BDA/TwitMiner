package extracteurMotifsFrequents;

import java.io.IOException;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
        CsvToTrans csv = new CsvToTrans("amour-100.000-tweets.csv");
        @SuppressWarnings("unused")
		TransToCsv trans = new TransToCsv("Apriori\\src\\result.trans");
        csv.run();
        //trans.run();
    }
}
