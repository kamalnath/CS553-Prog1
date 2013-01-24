/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BenchCommonUtils;

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
            cleanJvm();
        }
    }

    protected void calculateStats() throws IllegalStateException {
        double[] times = getTimes();
        double[] sampleSorted = times.clone();	// must make a clone since do not want the sort side effect to affect the caller
        Arrays.sort(sampleSorted);
        System.out.print(" Latency Median : " + CalcSupport.median(sampleSorted));
        System.out.print("  Mean : " + CalcSupport.mean(sampleSorted));

        if (task instanceof Runnable) {
            System.out.println(" Parallel Through put : " + (params.numberMeasurements * 2 / CalcSupport.sum(sampleSorted)));
        } else {
            System.out.println(" Sequential Through put : " + (params.numberMeasurements / CalcSupport.sum(sampleSorted)));
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
        for (long start = System.nanoTime(); System.nanoTime() - start < params.getWarmupTime() * 1e9; n *= 2) {	// * 1e9 converts warmupTime to ns
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
        if (task instanceof Callable) {
            Callable callable = (Callable) task;
            t1 = timeNs();
            callable.call();
        } else if (task instanceof Runnable) {
            Runnable runnable = (Runnable) task;
            ArrayList<Thread> arrT = new ArrayList<Thread>();
            for (int i = 0; i < 2; i++) {
                arrT.add(new Thread(runnable));
            }
            t1 = timeNs();
            for (Thread tr : arrT) {
                tr.start();
            }

            for (Thread tr : arrT) {
                tr.join();
            }
        } else {
            throw new IllegalStateException("task is neither a Callable or Runnable--this should never happen");
        }
        long t2 = timeNs();
        return new Measurement(timeDiffSeconds(t1, t2));
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
        return diff;
        //return diff * 1e-9;
    }
}
