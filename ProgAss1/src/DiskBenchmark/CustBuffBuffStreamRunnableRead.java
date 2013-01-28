/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DiskBenchmark;

import BenchCommonUtils.RandomUtils;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 *
 * @author kamalnath_ng
 */
public class CustBuffBuffStreamRunnableRead implements MyRunnable {

    InputStream fis;
    byte[] fbuf;
    static int i;
    long overheadtime = 0;

    public CustBuffBuffStreamRunnableRead(byte[] fbuf) {
        this.fbuf = fbuf;
    }

    public InputStream getFis() {
        return fis;
    }

    public void setFis(InputStream fis) {
        this.fis = fis;
    }

    public byte[] getFbuf() {
        return fbuf;
    }

    public void setFbuf(byte[] fbuf) {
        this.fbuf = fbuf;
    }

    @Override
    public void run() {
        try {
            long overheadtime = 0;
            long startfilemake = System.nanoTime();
            fis = new BufferedInputStream(new FileInputStream(RandomUtils.getFilepathRead() + i));
            overheadtime = System.nanoTime() - startfilemake;
            i++;
            overheadtime += DiskBenchUtil.customBufferBufferedStreamRead(fis, fbuf);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }finally{
            setOverheadtime(overheadtime);
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
