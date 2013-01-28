/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DiskBenchmark;

import BenchCommonUtils.RandomUtils;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/** 
 *
 * @author kamalnath_ng
 */
public class CustBuffBuffStreamRunnableWrite implements MyRunnable {

    OutputStream fos;
    int ibufflen;
    byte[] fbuf;
    private long overheadtime;

    public CustBuffBuffStreamRunnableWrite( int ibufflen, byte[] fbuf) {
        this.ibufflen = ibufflen;
        this.fbuf = fbuf;
    }

    @Override
    public void run() {
        try {
            long overheadtime = 0;
            long startfilemake = System.nanoTime();
            fos = new BufferedOutputStream(new FileOutputStream(RandomUtils.getRandFileName()));
            overheadtime = System.nanoTime() - startfilemake;
            
           overheadtime += DiskBenchUtil.customBufferBufferedStreamWrite(fos, ibufflen,  fbuf);
        } catch (FileNotFoundException ex) {
           ex.printStackTrace();
        }
        finally{
            setOverheadtime(overheadtime);
        }
    }

    public OutputStream getFos() {
        return fos;
    }

    public void setFos(OutputStream fos) {
        this.fos = fos;
    }

   

    public byte[] getFbuf() {
        return fbuf;
    }

    public void setFbuf(byte[] fbuf) {
        this.fbuf = fbuf;
    }

    public int getIbufflen() {
        return ibufflen;
    }

    public void setIbufflen(int ibufflen) {
        this.ibufflen = ibufflen;
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

