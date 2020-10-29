import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SingleThreadImpl {

    int mIterationNum = 0;
    int mDecimalPlacesRead;
    BigDecimal mResult = new BigDecimal("0");

    SingleThreadImpl() {

        BigDecimal tSampleToCompare = Utils.readValueFromFile("src\\main\\resources\\pi_100k.txt");
        mDecimalPlacesRead = tSampleToCompare.scale();
        Logger lg = MainLogger.getInstance();

        BigDecimal t16Bd = new BigDecimal("16");
        BigDecimal t1Bd = new BigDecimal("1");
        BigDecimal t2Bd = new BigDecimal("2");
        BigDecimal t4Bd = new BigDecimal("4");
        BigDecimal t5Bd = new BigDecimal("5");
        BigDecimal t6Bd = new BigDecimal("6");
        BigDecimal t8Bd = new BigDecimal("8");

        long start_time = System.currentTimeMillis();
        for (int i = 0; i < mDecimalPlacesRead; i++) {

            BigDecimal t16PowBd = t16Bd.pow(mIterationNum);

            BigDecimal tFirstMult = t1Bd.divide(t16PowBd, 100000, RoundingMode.HALF_EVEN);

            BigDecimal t8MultItNumBd = t8Bd.multiply(new BigDecimal(mIterationNum));

            BigDecimal tSumm1 = t8MultItNumBd.add(t1Bd);
            BigDecimal tSumm2 = t8MultItNumBd.add(t4Bd);
            BigDecimal tSumm3 = t8MultItNumBd.add(t5Bd);
            BigDecimal tSumm4 = t8MultItNumBd.add(t6Bd);

            BigDecimal t1stSubResultBd = t4Bd.divide(tSumm1, 100000, RoundingMode.HALF_EVEN);
            BigDecimal t2ndSubResultBd = t2Bd.divide(tSumm2, 100000, RoundingMode.HALF_EVEN);
            BigDecimal t3rdSubResultBd = t1Bd.divide(tSumm3, 100000, RoundingMode.HALF_EVEN);
            BigDecimal t4thSubResultBd = t1Bd.divide(tSumm4, 100000, RoundingMode.HALF_EVEN);

            BigDecimal tSubResultsSubtractedBd = t1stSubResultBd.subtract(t2ndSubResultBd);
            tSubResultsSubtractedBd = tSubResultsSubtractedBd.subtract(t3rdSubResultBd);
            tSubResultsSubtractedBd = tSubResultsSubtractedBd.subtract(t4thSubResultBd);

            BigDecimal tFinalResultBd = tFirstMult.multiply(tSubResultsSubtractedBd);

            mResult = mResult.add(tFinalResultBd);
            mIterationNum = mIterationNum + 1;
            if (i % 1000 == 0) {
                long end_time = System.currentTimeMillis();
                double differenceSecs = (end_time - start_time) / 1000F;
                long st1_time = System.currentTimeMillis();
                int tMatchedNumbers = Utils.getNumbersMatched(mResult, tSampleToCompare);
                long end1_time = System.currentTimeMillis();
                double diffCalcTime = end1_time - st1_time;
                lg.info("Total processed iterations: " + i + " from " + mDecimalPlacesRead + ", time: " + differenceSecs + ", time to calc diff: " + diffCalcTime + ", numbers matched: " + tMatchedNumbers);
                start_time = System.currentTimeMillis();
            }
        }

        BigDecimal tTruncatedRes = mResult.setScale(mDecimalPlacesRead, RoundingMode.FLOOR);

        lg.info(tTruncatedRes.compareTo(tSampleToCompare));

    }

}
