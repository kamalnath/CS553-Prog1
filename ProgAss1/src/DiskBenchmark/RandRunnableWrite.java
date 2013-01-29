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

    public RandRunnableWrite(byte[] fbuf) {
        this.fbuf = fbuf;
    }

    @Override
    public void run() {
        long ovrheadtime = 0;
        try {
            long startfilemake = System.nanoTime();
            RandomAccessFile file = new RandomAccessFile(RandomUtils.getRandFileName(), "rw");
            ovrheadtime = System.nanoTime() - startfilemake;
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
