import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        CSVToTrans csv = new CSVToTrans("amour-100.000-tweets.csv");
        TransToCSV trans = new TransToCSV("Apriori\\src\\result.trans");
        csv.run();
        //trans.run();
    }
}
