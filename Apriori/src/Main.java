import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        CSVToTrans csv = new CSVToTrans("Apriori\\src\\test.csv");

        csv.run();
    }
}
