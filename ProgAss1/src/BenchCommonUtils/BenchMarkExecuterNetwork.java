/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BenchCommonUtils;

import DiskBenchmark.*;
import NetworkBench.TCPChatClientCallable;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author kamalnath_ng
 */
public class BenchMarkExecuterNetwork {

    private Params params;
    private Object task;
    private static final int maxRestoreJvmLoops = 100;
    protected Measurement[] measurements;
    protected long numberOfThreads = 2;

    public BenchMarkExecuterNetwork(Object job, Params params) {
        try {
            perform(job, params);
        } catch (Exception ex) {
            ex.printStackTrace();
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
        System.out.print("		LATENCY  : second(s)/operation [ min :" + sampleSorted[0] + " | max : " + sampleSorted[sampleSorted.length - 1] + " | median : " + CalcSupport.median(sampleSorted));
        System.out.println(" | mean : " + CalcSupport.mean(sampleSorted) + " ]  ");

        if (task instanceof Runnable) {
            System.out.println("		THROUGHPUT :(MB/sec) " + ((params.numberMeasurements * 2 * params.getFactor()) / CalcSupport.sum(sampleSorted)));
        } else {
            System.out.println("		THROUGHPUT :(MB/sec) " + ((params.numberMeasurements * params.getFactor()) / CalcSupport.sum(sampleSorted)));
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
        for (int i = 0; i < params.getWarmupTime(); i++) {
            measure(n);
        }
    }

    private void cleanJvm() {
        long memUsedPrev = memoryUsed();
        for (int i = 0; i < maxRestoreJvmLoops; i++) {
            System.runFinalization();
            System.gc();
            long memUsedNow = memoryUsed();
            if ((ManagementFactory.getMemoryMXBean().getObjectPendingFinalizationCount() == 0)
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
        if (task instanceof MyCallable) {
            MyCallable objCallable = (MyCallable) task;
            if(task instanceof TCPChatClientCallable){
                TCPChatClientCallable objTCPChatClientCallable =(TCPChatClientCallable) task;
                objCallable  = objTCPChatClientCallable.clone();
            }
            t1 = timeNs();
            objCallable.call();
        } else if (task instanceof Runnable) {
            ArrayList<MyThread> arrT = new ArrayList<MyThread>();
            runnable = (MyRunnable) task;
            for (int i = 0; i < params.numberThreads; i++) {
                arrT.add(new MyThread(runnable.clone()));
            }
            t1 = timeNs();
            for (MyThread tr : arrT) {
                tr.start();
            }
            for (MyThread tr : arrT) {
                tr.join();
                Closeable objCloseable =tr.getObjMyRunnable().getClose();
                 if(objCloseable!=null){
                    objCloseable.close();
                 }
            }
        } else {
            throw new IllegalStateException("task is neither a Callable or Runnable--this should never happen");
        }

        long t2 = timeNs();
        return new Measurement(timeDiffSeconds(t1, (t2 - overheadtime)));
    }

    protected long timeNs() {
        if (params.getMeasureCpuTime()) {
            return ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
        }
        long time = System.nanoTime();

        return time;
    }

    public static double timeDiffSeconds(long t1, long t2) throws IllegalArgumentException {
        if (t1 > t2) {
            throw new IllegalArgumentException("clock ran backwards: t1 = " + t1 + " > t2 = " + t2);
        }
        long diff = t2 - t1;
        //System.out.println(diff * 1e-9);
        return diff * 1e-9;
    }
}
