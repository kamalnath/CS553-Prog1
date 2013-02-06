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
import java.util.Scanner;

public class BenchDiskMain {

    static String filePath;
    private static final Params params = new Params();
    public static void main(String[] args) throws FileNotFoundException, IOException {
        try {
            Scanner sc = new Scanner(System.in);
            boolean flag=true;
            while(flag){
            System.out.println("Please enter the tmp File location to proceed");
            filePath = sc.nextLine();
            if(!filePath.equalsIgnoreCase("")){
                flag=false;
            }
            }
            benchText(filePath);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private static void benchText(String filePath) throws FileNotFoundException, InterruptedException, IOException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
            System.out.println("                      Select your choice                               ");
            System.out.println("  0  >------>          Set up environment     <--------------<   ");
            System.out.println("  1  >------>          Bencmark Reads         <--------------<   ");
            System.out.println("  2  >------>          Benchmark Write        <--------------<   ");
            System.out.println("  3  >------>             To Exit             <--------------<   ");
            System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
            switch (sc.nextInt()) {
                case 0:
                    setuEnv();
                    break;
                case 1:
                    showReadmenu();
                    break;
                case 2:
                    showWritemenu();
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }
    }

    private static void benchWrite(final int iType, String filePath) throws FileNotFoundException, InterruptedException, IOException {

        byte[] buf;
        int bufflen = 0;
        switch (iType) {
            case 1:
                System.out.println("");
                System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
                System.out.println("Start WRITE benchmark with little block (1 B)");
                params.setWarmupTime(10);
                params.setNumberMeasurements(5000);
                buf = new byte[1];
                bufflen = 1;
                break;
            case 2:
                System.out.println("");
                System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
                System.out.println("Start WRITE benchmark with little block (1 KB)");
                params.setWarmupTime(10);
                params.setNumberMeasurements(5000);
                buf = new byte[1024];
                bufflen = 1024;
                break;
            case 3:
                System.out.println("");
                System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
                System.out.println("Start WRITE benchmark with little block (1 MB)");
                params.setNumberMeasurements(4000);
                params.setWarmupTime(10);
                buf = new byte[1024 * 1024];
                bufflen = buf.length;
                break;
            case 4:
                System.out.println("");
                System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
                System.out.println("Start WRITE benchmark with little block (1 GB)");
                params.setNumberMeasurements(4);
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
                System.out.println("");
                System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
                System.out.println("Start READ benchmark with little block (1 B)");
                params.setWarmupTime(10);
                params.setNumberMeasurements(1000);
                buf = new byte[1];
                break;
            case 2:
                System.out.println("");
                System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
                System.out.println("Start READ benchmark with little block (1 KB)");
                params.setWarmupTime(10);
                params.setNumberMeasurements(1000);
                buf = new byte[512];
                break;
            case 3:
                System.out.println("");
                System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
                System.out.println("Start READ benchmark with little block (1 MB)");
                params.setNumberMeasurements(1000);
                params.setWarmupTime(10);
                buf = new byte[1024 * 1024];
                break;
            case 4:
                System.out.println("");
                System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
                System.out.println("Start READ benchmark with little block (1 GB)");
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
        params.setFactor((double) buf.length / (1024 * 1024));
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

    private static void showReadmenu() throws FileNotFoundException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        boolean flag=true;
        while (flag) {
            System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
            System.out.println("                     Select your choice                               ");
            System.out.println("  0  >------> Run all READ Bench marks for all size blocks <--------------<   ");
            System.out.println("  1  >------>      Run all READ Bench marks for 1 Byte     <--------------<   ");
            System.out.println("  2  >------>      Run all READ Bench marks for 1 KB       <--------------<   ");
            System.out.println("  3  >------>      Run all READ Bench marks for 1 MB       <--------------<   ");
            System.out.println("  4  >------>      Run all READ Bench marks for 1 GB       <--------------<   ");
            System.out.println("  5  >------>                     To return                <--------------<   ");
            System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
            switch (sc.nextInt()) {
                case 0:
                    benchRead(1);
                    benchRead(2);
                    benchRead(3);
                    break;
                case 1:
                    benchRead(1);
                    break;
                case 2:
                    benchRead(2);
                    break;
                case 3:
                    benchRead(3);
                    break;
                case 4:
                    benchRead(4);
                    break;
                default:flag=false;
                    break;
            }
        }
    }

    static void showWritemenu() throws IOException, FileNotFoundException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        boolean flag=true;
        while (flag) {
            System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
            System.out.println("                     Select your choice                               ");
            System.out.println("  0  >------> Run all write Bench marks for all size blocks <--------------<   ");
            System.out.println("  1  >------>      Run all write Bench marks for 1 Byte     <--------------<   ");
            System.out.println("  2  >------>      Run all write Bench marks for 1 KB       <--------------<   ");
            System.out.println("  3  >------>      Run all write Bench marks for 1 MB       <--------------<   ");
            System.out.println("  4  >------>      Run all write Bench marks for 1 GB       <--------------<   ");
            System.out.println("  5  >------>                     To return                 <--------------<   ");
            System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
            switch (sc.nextInt()) {
                case 0:
                    benchWrite(1, filePath);
                    benchWrite(2, filePath);
                    benchWrite(3, filePath);
                    break;
                case 1:
                    benchWrite(1, filePath);
                    break;
                case 2:
                    benchWrite(2, filePath);
                    break;
                case 3:
                    benchWrite(3, filePath);
                    break;
                case 4:
                    benchWrite(4, filePath);
                    break;
                default:flag=false;
                    break;
            }
        }
    }

    public static void setuEnv() throws FileNotFoundException {
         System.out.println("Folder structure and Dummy file[s] creation stated ");
        File newFolder = new File(filePath + RandomUtils.getFilepathRead());
        newFolder = new File(filePath + RandomUtils.getFilepathRandRead());
        newFolder.mkdirs();
        newFolder = new File(filePath + RandomUtils.getFilepathWrite());
        newFolder.mkdirs();
        System.out.println("This will take some time please wait[ approx 3 to 5 mins] ");
        BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(filePath + RandomUtils.getFilepathRead()));
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
        for (int i = 0; i < 4; i++) {
            makeDummyFile(filePath + RandomUtils.getFilepathRead() + i);

        }
        System.out.println("Dummy Files for read operations made ");
        for (int i = 0; i < 4; i++) {
            makeDummyFile(filePath + RandomUtils.getFilepathRandRead() + i);

        }
        System.out.println("Dummy Files for Randome read operations made ");
        System.out.println("Folder structure and Dummy file[s] creation done");
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
}
