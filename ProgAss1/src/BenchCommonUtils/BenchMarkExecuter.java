/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BenchCommonUtils;

import DiskBenchmark.*;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kamalnath_ng
 */
public class BenchMarkExecuter {

    private Params params;
    private Object task;
    private static final int maxRestoreJvmLoops = 100;
    protected Measurement[] measurements;
    protected long numberOfThreads = 2;

    public BenchMarkExecuter(Object job, Params params) {
        try {
            perform(job, params);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(BenchMarkExecuter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(BenchMarkExecuter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(BenchMarkExecuter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void perform(Object task, Params params) throws IllegalArgumentException, IllegalStateException, Exception {
        this.task = task;
        this.params = params;
        try {
            cleanJvm();
            if (params.getManyExecutions()) {
                warmupJvm();
                doMeasurements();
                calculateStats();
                
            }
        } finally {
            RandomUtils.fileCleanup(params.getTempPath());
            cleanJvm();
        }
    }

    protected void calculateStats() throws IllegalStateException {
        double[] times = getTimes();
        double[] sampleSorted = times.clone();	// must make a clone since do not want the sort side effect to affect the caller
        Arrays.sort(sampleSorted);
        System.out.print("		LATENCY  : second(s)/operation [ min :"+sampleSorted[0]+" | max : "+sampleSorted[sampleSorted.length-1]+" | median : " + CalcSupport.median(sampleSorted));
        System.out.println(" | mean : " + CalcSupport.mean(sampleSorted) +" ]  ");

        if (task instanceof Runnable) {
            System.out.println("		THROUGHPUT :(MB/sec) " + ((params.numberMeasurements * 2 * params.getFactor())/ CalcSupport.sum(sampleSorted)));
        } else {
            System.out.println("		THROUGHPUT :(MB/sec) " + ((params.numberMeasurements *params.getFactor())/ CalcSupport.sum(sampleSorted)));
        }
    }

    protected double[] getTimes() throws IllegalStateException {

        double[] times = new double[measurements.length];
        for (int i = 0; i < times.length; i++) {
            times[i] = measurements[i].executionTime;
        }
        return times;
    }

    protected void doMeasurements() throws Exception {
        cleanJvm();
        measurements = new Measurement[params.getNumberMeasurements()];
        int total = 0;
        for (int i = 0; i < measurements.length; i++, total++) {
            measurements[i] = measure(numberOfThreads);
        }
    }

    protected void warmupJvm() throws Exception {
        cleanJvm();
        long n = 1;
        for (int i=0;i<params.getWarmupTime() ; i ++) {	// * 1e9 converts warmupTime to ns
            measure(n);
        }
    }

    private void cleanJvm() {
        long memUsedPrev = memoryUsed();
        for (int i = 0; i < maxRestoreJvmLoops; i++) {
            System.runFinalization();
            System.gc();
            long memUsedNow = memoryUsed();
            if ( // break early if have no more finalization and get constant mem used
                    (ManagementFactory.getMemoryMXBean().getObjectPendingFinalizationCount() == 0)
                    && (memUsedNow >= memUsedPrev)) {

                break;
            } else {
                memUsedPrev = memUsedNow;
            }
        }

    }

    public static long memoryUsed() {
        Runtime rt = Runtime.getRuntime();
        return rt.totalMemory() - rt.freeMemory();
    }

    protected Measurement measure(long n) throws IllegalArgumentException, Exception {
        long t1 = -1;
        MyRunnable runnable;
        long overheadtime = 0;
        if (task instanceof Callable) {
            Callable objCallable = (Callable) task;
            //objCallable = processCallableTask();
            t1 = timeNs();
            overheadtime = (Long)objCallable.call();
        } else if (task instanceof Runnable) {
            ArrayList<MyThread> arrT = new ArrayList<MyThread>();
            //arrT = processRunnableTask(arrT);
            runnable = (MyRunnable) task;
            for (int i = 0; i < params.numberThreads; i++) {
                arrT.add(new MyThread(runnable));
            }
            t1 = timeNs();
            for (MyThread tr : arrT) {
                tr.start();
            }
            for (MyThread tr : arrT) {
                tr.join();
                overheadtime += tr.getObjMyRunnable().getOverheadtime();
            }
        } else {
            throw new IllegalStateException("task is neither a Callable or Runnable--this should never happen");
        }

        long t2 = timeNs();
        return new Measurement(timeDiffSeconds(t1, (t2 - 0)));
    }

    protected long timeNs() {
        if (params.getMeasureCpuTime()) {
            return ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
        }
        return System.nanoTime();
    }

    public static double timeDiffSeconds(long t1, long t2) throws IllegalArgumentException {
        if (t1 > t2) {
            throw new IllegalArgumentException("clock ran backwards: t1 = " + t1 + " > t2 = " + t2);
        }
        long diff = t2 - t1;
        //return diff;
        return diff * 1e-9;
    }

//    private ArrayList processRunnableTask(ArrayList<Thread> arrT) throws FileNotFoundException {
//        InputStream fis;
//        OutputStream fos;
//        if (params.isWriteOP) {
//            CustBuffBuffStreamRunnableWrite runnable = (CustBuffBuffStreamRunnableWrite) task;
//            for (int i = 0; i < 2; i++) {
//                fos = new BufferedOutputStream(new FileOutputStream(RandomUtils.getRandFileName()));
//                runnable.setFos(fos);
//                arrT.add(new Thread(runnable));
//            }
//        } else {
//            CustBuffBuffStreamRunnableRead runnable = (CustBuffBuffStreamRunnableRead) task;
//            for (int i = 0; i < params.numberThreads; i++) {
//                fis = new BufferedInputStream(new FileInputStream(RandomUtils.getFilepathRead() + i));
//                runnable.setFis(fis);
//                arrT.add(new Thread(runnable));
//            }
//        }
//        return arrT;
//    }
//
//    private Callable processCallableTask() throws FileNotFoundException {
//        OutputStream fos;
//        CustBuffBuffStreamCallableWrite callableW;
//        CustBuffBuffStreamCallableRead callableR;
//        InputStream fis;
//        if (params.isWriteOP) {
//            callableW = (CustBuffBuffStreamCallableWrite) task;
//            fos = new BufferedOutputStream(new FileOutputStream(RandomUtils.getRandFileName()));
//            callableW.setFos(fos);
//            return callableW;
//
//        } else {
//            fis = new BufferedInputStream(new FileInputStream(RandomUtils.getFilepathRead()));
//            callableR = (CustBuffBuffStreamCallableRead) task;
//            callableR.setFis(fis);
//            return callableR;
//        }
//
//    }
}
