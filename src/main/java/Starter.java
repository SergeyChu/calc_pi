import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Starter {

    static volatile BigDecimal mResult = new BigDecimal(0);
    static volatile int mIterationNum = 0;
    static BigDecimal mSampleToCompare;
    static int mDecimalPlacesRead;

    public static void main(String[] args) throws FileNotFoundException {

        new MultiThreadImpl();

    }



}
