import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CalcThread implements Runnable {

    private final AtomicInteger mIterationNum;
    private final int mDecimalPlacesRead;
    private final AtomicReference<BigDecimal> mResult;
    private final BigDecimal mEtalonToCompare;
    private final ReadWriteLock mLock;
    private final Logger lg;

    CalcThread(AtomicInteger pIterationNum, int pDecimalPlacesRead, AtomicReference<BigDecimal> pResult, BigDecimal pEtalonToCompare, ReadWriteLock pLock) {
        mIterationNum = pIterationNum;
        mDecimalPlacesRead = pDecimalPlacesRead;
        mResult = pResult;
        mEtalonToCompare  = pEtalonToCompare;
        lg = MainLogger.getInstance();
        mLock = pLock;
    }

    public void run() {

        BigDecimal t16Bd = new BigDecimal("16");
        BigDecimal t1Bd = new BigDecimal("1");
        BigDecimal t2Bd = new BigDecimal("2");
        BigDecimal t4Bd = new BigDecimal("4");
        BigDecimal t5Bd = new BigDecimal("5");
        BigDecimal t6Bd = new BigDecimal("6");
        BigDecimal t8Bd = new BigDecimal("8");

        long start_time = System.currentTimeMillis();

        for (int i = 0; i < mDecimalPlacesRead; i++) {

            long start_time_single_it = System.currentTimeMillis();
            //Getting value by reference to local variable and moving counter,
            // so next thread can start working on different iteration.
            int tLocalIterationNum = mIterationNum.getAndIncrement();

            lg.trace(Thread.currentThread().getName() + " Got local iteration num: " + tLocalIterationNum);

            BigDecimal t16PowBd = t16Bd.pow(tLocalIterationNum);

            BigDecimal tFirstMult = t1Bd.divide(t16PowBd, 100000, RoundingMode.HALF_EVEN);

            BigDecimal t8MultItNumBd = t8Bd.multiply(new BigDecimal(tLocalIterationNum));

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

            mLock.writeLock().lock();
            mResult.set(mResult.get().add(tFinalResultBd));
            mLock.writeLock().unlock();

            long end_time_single_it = System.currentTimeMillis();
            double diffItTime = (end_time_single_it - start_time_single_it);
            lg.trace("Time of a single iteration: " + diffItTime);

            if (i % 100 == 0) {
                long end_time = System.currentTimeMillis();
                double differenceSecs = (end_time - start_time) / 1000F;
                long st1_time = System.currentTimeMillis();

                //The call is synchronized
                int tMatchedNumbers = Utils.getNumbersMatched(mResult.get(), mEtalonToCompare);

                long end1_time = System.currentTimeMillis();
                double diffCalcTime = (end1_time - st1_time) / 1000F;
                lg.info(Thread.currentThread().getName() +": Total processed iterations: " + i + " from " + mDecimalPlacesRead + ", time: " + differenceSecs + ", time to calc diff: " + diffCalcTime + ", numbers matched: " + tMatchedNumbers);
                if (tMatchedNumbers >= mDecimalPlacesRead) {break;}
                start_time = System.currentTimeMillis();
            }
        }


    }

}
