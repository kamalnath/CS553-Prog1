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

    public RandRunnableRead(byte[] fbuf) {
        this.fbuf = fbuf;
    }

    @Override
    public void run() {
        long ovrheadtime = 0;
        try {
            
            long startfilemake = System.nanoTime();
            RandomAccessFile file = new RandomAccessFile(RandomUtils.getFilepathRead() + i, "r");
            ovrheadtime = System.nanoTime() - startfilemake;
            i++;
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
