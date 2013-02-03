/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DiskBenchmark;

import BenchCommonUtils.RandomUtils;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 *
 * @author USER
 */
public class RandRunnableWrite implements MyRunnable {

    private byte[] fbuf;
    private long overheadtime;
    String filePath;

    public RandRunnableWrite(byte[] fbuf,String filePath) {
        this.fbuf = fbuf;
        this.filePath= filePath;
    }

    @Override
    public void run() {
        long ovrheadtime = 0;
        try {
            long startfilemake = System.nanoTime();
            RandomAccessFile file = new RandomAccessFile(filePath+RandomUtils.getRandFileName(), "rw");
            ovrheadtime = System.nanoTime() - startfilemake;
            //System.out.println("fbuf------" +fbuf.length);
            ovrheadtime += DiskBenchUtil.RandFileWrite(file, fbuf);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            setOverheadtime(ovrheadtime);
        }
    }

    @Override
    public long getOverheadtime() {
        return overheadtime;
    }

    @Override
    public void setOverheadtime(long time) {
        overheadtime = time;
    }
}
