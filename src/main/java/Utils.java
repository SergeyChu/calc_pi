import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

public class Utils {

    //src\main\resources\pi.txt
    static BigDecimal readValueFromFile(String tFilePath) {

        String tValueRead;
        BigDecimal tResultRead = new BigDecimal("0");
        Logger lg = MainLogger.getInstance();

        lg.info("Reading the file: " + tFilePath);
        long start_time = System.currentTimeMillis();
        try (BufferedReader br = new BufferedReader(new FileReader(tFilePath))) {
            tValueRead = br.readLine();
            tResultRead = new BigDecimal(tValueRead);
            long end_time = System.currentTimeMillis();
            double difference = (end_time - start_time);
            lg.info("Done with reading the file in(ms): " + difference + " total lenght: " + tValueRead.length());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tResultRead;
    }

    static synchronized int getNumbersMatched(BigDecimal pValueToCompare, BigDecimal pEtalonValue) {
        int result = 0;
        String tValueToCompare = pValueToCompare.toPlainString();
        String tEtalonValue = pEtalonValue.toPlainString();

        for (int i = 0; i < tValueToCompare.length() && i < tEtalonValue.length(); i++) {
            if (tValueToCompare.charAt(i) == tEtalonValue.charAt(i)) {
                result++;
            } else { break; }
        }

        return result;
    }
}
