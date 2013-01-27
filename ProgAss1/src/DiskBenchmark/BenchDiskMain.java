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
import BenchCommonUtils.Params;
import java.io.*;

public class BenchDiskMain {

    private static int BUFFER = 8192;
    private static final Params params = new Params();

    public static void main(String[] args) throws FileNotFoundException, IOException {
        benchText();
    }

    private static void benchText() throws FileNotFoundException {
        
//        System.out.println("Start WRITE benchmark with little block (1 B)");
//        benchWrite(1);
//        System.out.println("Start WRITE benchmark with medium block (1 KB)");
//        benchWrite(2);
//        System.out.println("Start WRITE benchmark with medium block (1 MB)");
//        benchWrite(3);
//        System.out.println("Start WRITE benchmark with medium block (1 GB)");
//        benchWrite(4);
        
//            benchRead(1);
//            benchRead(2);
//            benchRead(3);
//            benchRead(4);
    }

    private static void benchWrite( final int iType) throws FileNotFoundException {

        byte[] buf;
        int bufflen = 0;
        switch (iType) {
            case 1:
                buf = new byte[1];
                bufflen = 1;
                break;
            case 2:
                params.setWarmupTime(10);
                params.setNumberMeasurements(1000);
                buf = new byte[512];
                bufflen = 512;
                break;
            case 3:
                params.setNumberMeasurements(1000);
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
                params.setNumberMeasurements(1);
                params.setWarmupTime(1);
                buf = new byte[1024 * 1024 * 400];
                 bufflen = buf.length;
                break;
        }
        final int ibufflen = bufflen;
        final byte[] fbuf = buf;
        params.setIsWriteOP(true);
        CustBuffBuffStreamCallableWrite customBufferBufferedStreamCallable = new CustBuffBuffStreamCallableWrite( ibufflen, fbuf);
        BenchMarkExecuter benchCustomBufferBufferedStreamSeq = new BenchMarkExecuter(customBufferBufferedStreamCallable, params);
        
        RandCallableWrite objRandCallableWrite = new RandCallableWrite(fbuf);
        BenchMarkExecuter benchRandCallableWrite = new BenchMarkExecuter(objRandCallableWrite, params);
        
        params.setNumberThreads(2);
        CustBuffBuffStreamRunnableWrite customBufferBufferedStreamRunnable = new CustBuffBuffStreamRunnableWrite( ibufflen, fbuf);
        BenchMarkExecuter benchCustomBufferBufferedStreamParallel = new BenchMarkExecuter(customBufferBufferedStreamRunnable, params);
        
        RandRunnableWrite objRandRunnableWrite = new RandRunnableWrite(  fbuf);
        BenchMarkExecuter RandRunnableWriteParallel = new BenchMarkExecuter(objRandRunnableWrite, params);
        
        
        
    }
    
    private static void benchRead( final int iType) throws FileNotFoundException {

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
                params.setNumberMeasurements(1);
                params.setWarmupTime(2);
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
        CustBuffBuffStreamCallableRead customBufferBufferedStreamCallable = new CustBuffBuffStreamCallableRead(  fbuf);
        BenchMarkExecuter benchCustomBufferBufferedStreamSeq = new BenchMarkExecuter(customBufferBufferedStreamCallable, params);
        
        RandCallableRead objRandCallableRead = new RandCallableRead(fbuf);
        BenchMarkExecuter benchRandCallableRead = new BenchMarkExecuter(objRandCallableRead, params);
        
        params.setNumberThreads(2);
        
        CustBuffBuffStreamRunnableRead customBufferBufferedStreamRunnable = new CustBuffBuffStreamRunnableRead( fbuf);
        BenchMarkExecuter benchCustomBufferBufferedStreamParallel = new BenchMarkExecuter(customBufferBufferedStreamRunnable, params);
        
        RandRunnableRead objRandRunnableRead = new RandRunnableRead(fbuf);
        BenchMarkExecuter benchRandParallel = new BenchMarkExecuter(objRandRunnableRead, params);
    }
    
}
