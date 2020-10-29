import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MultiThreadImpl {

    AtomicInteger mIterationNum = new AtomicInteger(0);
    volatile int mDecimalPlacesRead;
    volatile AtomicReference<BigDecimal> mResult = new AtomicReference<>(new BigDecimal("0"));

    MultiThreadImpl() {

        BigDecimal tSampleToCompare = Utils.readValueFromFile("src\\main\\resources\\pi_100k.txt");
        mDecimalPlacesRead = tSampleToCompare.scale();

        Runnable tMyRunnable = new CalcThread(mIterationNum, mDecimalPlacesRead, mResult, tSampleToCompare);
        Thread th1 = new Thread(tMyRunnable, "thread1");
        th1.start();

        Runnable tMyRunnable1 = new CalcThread(mIterationNum, mDecimalPlacesRead, mResult, tSampleToCompare);
        Thread th2 = new Thread(tMyRunnable1, "thread2");
        th2.start();

        Runnable tMyRunnable2 = new CalcThread(mIterationNum, mDecimalPlacesRead, mResult, tSampleToCompare);
        Thread th3 = new Thread(tMyRunnable2, "thread3");
        th3.start();

        Runnable tMyRunnable3 = new CalcThread(mIterationNum, mDecimalPlacesRead, mResult, tSampleToCompare);
        Thread th4 = new Thread(tMyRunnable3, "thread4");
        th4.start();

        Runnable tMyRunnable4 = new CalcThread(mIterationNum, mDecimalPlacesRead, mResult, tSampleToCompare);
        Thread th5 = new Thread(tMyRunnable4, "thread5");
        th5.start();

    }




}
