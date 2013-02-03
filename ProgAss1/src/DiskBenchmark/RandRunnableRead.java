/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DiskBenchmark;

import BenchCommonUtils.RandomUtils;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USER
 */
public class RandRunnableRead implements MyRunnable {

    private byte[] fbuf;
    private static int i;
    private long overheadtime;
    String filePath;
    public synchronized static int getI() {
        if (i > 2) {
            i = 0;
        } else {
            i++;
        }
        
        return i;
    }
    public RandRunnableRead(byte[] fbuf,String filePath) {
        this.fbuf = fbuf;
        this.filePath= filePath;
    }

    @Override
    public void run() {
        long ovrheadtime = 0;
        try {
            long startfilemake = System.nanoTime();
            RandomAccessFile file = new RandomAccessFile(filePath+RandomUtils.getFilepathRandRead() + getI(), "r");
            ovrheadtime = System.nanoTime() - startfilemake;
            ovrheadtime += DiskBenchUtil.RandFileRead(file, fbuf);
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
