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
public class CustBuffBuffStreamRunnableWrite implements Runnable {

    OutputStream fos;
    int ibufflen;
    byte[] fbuf;

    public CustBuffBuffStreamRunnableWrite( int ibufflen, byte[] fbuf) {
        this.ibufflen = ibufflen;
        this.fbuf = fbuf;
    }

    @Override
    public void run() {
        try {
            fos = new BufferedOutputStream(new FileOutputStream(RandomUtils.getRandFileName()));
           DiskBenchUtil.customBufferBufferedStreamWrite(fos, ibufflen,  fbuf);
        } catch (FileNotFoundException ex) {
           ex.printStackTrace();
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
    
}

