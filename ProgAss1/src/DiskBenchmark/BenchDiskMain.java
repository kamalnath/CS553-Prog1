/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author KamalNath_NG
 */
package DiskBenchmark;

import BenchCommonUtils.BenchMarkExecuter;
import BenchCommonUtils.CalcSupport;
import BenchCommonUtils.Params;
import BenchCommonUtils.RandomUtils;
import java.io.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BenchDiskMain {

    static String filePath;
    private static final Params params = new Params();

    public static void main(String[] args) throws FileNotFoundException, IOException {
        try {
            System.out.println("Please enter the tmp File location");
            filePath = "D:/Data/read";
            benchText(filePath);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    static void makeDummyFile(String path) {
        BufferedOutputStream fos = null;
        try {
            fos = new BufferedOutputStream(new FileOutputStream(path));
            byte[] buf = new byte[1024 * 1024 * 1024];
            try {
                fos.write(buf, 0, buf.length);
                fos.write(buf, 0, buf.length);
                fos.write(buf, 0, buf.length);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void benchText(String filePath) throws FileNotFoundException, InterruptedException, IOException {

//        File newFolder = new File(filePath + RandomUtils.getFilepathRead());
//        newFolder = new File(filePath + RandomUtils.getFilepathRandRead());
//        newFolder.mkdirs();
//        newFolder = new File(filePath + RandomUtils.getFilepathWrite());
//        newFolder.mkdirs();
//
//        BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(filePath + RandomUtils.getFilepathRead()));
//        byte[] buf = new byte[1024 * 1024 * 1024];
//        try {
//            fos.write(buf, 0, buf.length);
//            fos.write(buf, 0, buf.length);
//            fos.write(buf, 0, buf.length);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                fos.close();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//        for (int i = 0; i < 4; i++) {
//            makeDummyFile(filePath + RandomUtils.getFilepathRead() + i);
//
//        }
//        for (int i = 0; i < 4; i++) {
//            makeDummyFile(filePath + RandomUtils.getFilepathRandRead() + i);
//
//        }

        System.out.println("Start WRITE benchmark with little block (1 B)");

//        //benchWrite(1, filePath);
//        benchWrite(1, filePath);
//        benchWrite(2, filePath);
//        benchWrite(3, filePath);
//        System.out.println("Start WRITE benchmark with medium block (1 KB)");
//       
//        System.out.println("Start WRITE benchmark with medium block (1 MB)");
//        benchWrite(3);
//        System.out.println("Start WRITE benchmark with medium block (1 GB)");
//        benchWrite(4);

         System.out.println("--- READ benchmark with little block (1 B)--");
        benchRead(1);
         System.out.println("--- READ benchmark with little block (1 KB)--");
        benchRead(2);
        System.out.println("--- READ benchmark with little block (1 MB)--");
        benchRead(3);
         System.out.println("--- READ benchmark with little block (1 GB)--");
        benchRead(4);
    }

    private static void benchWrite(final int iType, String filePath) throws FileNotFoundException, InterruptedException, IOException {

        byte[] buf;
        int bufflen = 0;
        switch (iType) {
            case 1:
                params.setWarmupTime(10);
                params.setNumberMeasurements(5000);
                buf = new byte[1];
                bufflen = 1;
                break;
            case 2:
                params.setWarmupTime(10);
                params.setNumberMeasurements(5000);
                buf = new byte[1024];
                bufflen = 1024;
                break;
            case 3:
                params.setNumberMeasurements(4000);
                params.setWarmupTime(10);
                buf = new byte[1024 * 1024];
                bufflen = buf.length;
                break;
            case 4:
                params.setNumberMeasurements(1);
                params.setWarmupTime(1);
                buf = new byte[1024 * 1024 * 1024];
                bufflen = buf.length;
                break;
            default:
                params.setNumberMeasurements(10);
                params.setWarmupTime(1);
                buf = new byte[1024 * 1024 * 200];
                bufflen = buf.length;
                break;
        }
        final int ibufflen = bufflen;
        final byte[] fbuf = buf;

        params.setIsWriteOP(true);
        params.setTempPath(filePath);
        params.setFactor((double) buf.length / (1024 * 1024));
        System.out.println("");
        System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
        System.out.println("");
        System.out.println("--- Results for Single threaded Runs--");
        System.out.println("      Sequential File :");
        CustBuffBuffStreamCallableWrite customBufferBufferedStreamCallable = new CustBuffBuffStreamCallableWrite(ibufflen, fbuf, filePath);
        BenchMarkExecuter benchCustomBufferBufferedStreamSeq = new BenchMarkExecuter(customBufferBufferedStreamCallable, params);
        customBufferBufferedStreamCallable.getFos().close();
        Thread.sleep(30000);
        System.out.println("      Random Access File :");
        RandCallableWrite objRandCallableWrite = new RandCallableWrite(fbuf, filePath);
        BenchMarkExecuter benchRandCallableWrite = new BenchMarkExecuter(objRandCallableWrite, params);

        Thread.sleep(30000);
        System.out.println("");
        System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
        System.out.println("");
        System.out.println("--- Results for Multi threaded Runs--");
        System.out.println("     Sequential File :");
        params.setNumberThreads(2);
        CustBuffBuffStreamRunnableWrite customBufferBufferedStreamRunnable = new CustBuffBuffStreamRunnableWrite(ibufflen, fbuf, filePath);
        BenchMarkExecuter benchCustomBufferBufferedStreamParallel = new BenchMarkExecuter(customBufferBufferedStreamRunnable, params);
        Thread.sleep(30000);
        System.out.println("     Random Access File :");
        RandRunnableWrite objRandRunnableWrite = new RandRunnableWrite(fbuf, filePath);
        BenchMarkExecuter RandRunnableWriteParallel = new BenchMarkExecuter(objRandRunnableWrite, params);
        System.out.println("");
        System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
        System.out.println("");



    }

    private static void benchRead(final int iType) throws FileNotFoundException, InterruptedException {

        byte[] buf;
        params.setIsWriteOP(false);
        switch (iType) {
            case 1:
                params.setWarmupTime(10);
                params.setNumberMeasurements(1000);
                buf = new byte[1];
                break;
            case 2:
                params.setWarmupTime(10);
                params.setNumberMeasurements(1000);
                buf = new byte[512];
                break;
            case 3:
                params.setNumberMeasurements(1000);
                params.setWarmupTime(10);
                buf = new byte[1024 * 1024];
                break;
            case 4:
                params.setNumberMeasurements(3);
                params.setWarmupTime(1);
                buf = new byte[1024 * 1024 * 1024];
                break;
            default:
                params.setNumberMeasurements(1);
                params.setWarmupTime(2);
                buf = new byte[1024 * 1024 * 10];
                break;
        }
        final byte[] fbuf = buf;

        params.setIsWriteOP(false);
        params.setFactor((double)buf.length / (1024 * 1024));
        System.out.println("");
        System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
        System.out.println("");
        System.out.println("--- Results for Single threaded Runs--");
        System.out.println("      Sequential File :");
        CustBuffBuffStreamCallableRead customBufferBufferedStreamCallable = new CustBuffBuffStreamCallableRead(fbuf, filePath);
        BenchMarkExecuter benchCustomBufferBufferedStreamSeq = new BenchMarkExecuter(customBufferBufferedStreamCallable, params);
        Thread.sleep(10000);
        System.out.println("      Random Access File :");
        RandCallableRead objRandCallableRead = new RandCallableRead(fbuf, filePath);
        BenchMarkExecuter benchRandCallableRead = new BenchMarkExecuter(objRandCallableRead, params);
        Thread.sleep(10000);
        System.out.println("");
        System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
        System.out.println("");
        System.out.println("--- Results for Multi threaded Runs--");
        System.out.println("     Sequential File :");
        params.setNumberThreads(2);
        CustBuffBuffStreamRunnableRead customBufferBufferedStreamRunnable = new CustBuffBuffStreamRunnableRead(fbuf, filePath);
        BenchMarkExecuter benchCustomBufferBufferedStreamParallel = new BenchMarkExecuter(customBufferBufferedStreamRunnable, params);
        Thread.sleep(10000);
        System.out.println("     Random Access File :");
        RandRunnableRead objRandRunnableRead = new RandRunnableRead(fbuf, filePath);
        BenchMarkExecuter benchRandParallel = new BenchMarkExecuter(objRandRunnableRead, params);
    }

    private static void doSequential(int size) throws FileNotFoundException, IOException {
        byte[] buf = new byte[size];
        int bufflen = 1;
        int i = 0;
        long startfileclose = System.nanoTime();
        double[] sampleSorted = new double[100000];
        BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(filePath + RandomUtils.getRandFileName()));
        while (i < 100000) {
            long startwrite = System.nanoTime();
            try {
                fos.write(buf, 0, buf.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
            sampleSorted[i] = (System.nanoTime() - startwrite) * 1e-9;
            i++;
        }
        fos.close();
        Arrays.sort(sampleSorted);
        System.out.print("		LATENCY  : second(s)/operation [ min :" + sampleSorted[0] + " | max : " + sampleSorted[sampleSorted.length - 1] + " | median : " + CalcSupport.median(sampleSorted));
        System.out.println(" | mean : " + CalcSupport.mean(sampleSorted) + " ]  ");
        System.out.println("		THROUGHPUT :(MB/sec) " + ((100000 / CalcSupport.sum(sampleSorted)) * (size / (1024 * 1024))));
    }

    private static void doRandom(int size) throws FileNotFoundException, IOException {
        byte[] buf = new byte[size];
        int i = 0;
        double[] sampleSorted = new double[100000];
        RandomAccessFile file = new RandomAccessFile(filePath + RandomUtils.getRandFileName(), "rw");
        while (i < 100000) {
            long startwrite = System.nanoTime();
            try {
                int rand = RandomUtils.getRandomGenerator().nextInt(100);
                file.seek(rand);
                file.write(buf);
            } catch (Exception e) {
                e.printStackTrace();
            }
            sampleSorted[i] = (System.nanoTime() - startwrite) * 1e-9;
            i++;
        }
        file.close();
        Arrays.sort(sampleSorted);
        System.out.print("		LATENCY  : second(s)/operation [ min :" + sampleSorted[0] + " | max : " + sampleSorted[sampleSorted.length - 1] + " | median : " + CalcSupport.median(sampleSorted));
        System.out.println(" | mean : " + CalcSupport.mean(sampleSorted) + " ]  ");
        System.out.println("		THROUGHPUT :(MB/sec) " + ((100000 / CalcSupport.sum(sampleSorted)) * (size / (1024 * 1024))));
    }
}
