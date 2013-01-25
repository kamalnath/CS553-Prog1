/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DiskBenchmark;

import java.io.OutputStream;
import java.util.concurrent.Callable;

/**
 *
 * @author kamalnath_ng
 */
public class CustBuffBuffStreamCallableWrite implements Callable {

    OutputStream fos;
    int ibufflen;
    byte[] fbuf;

    @Override
    public Object call() {
        DiskBenchUtil.customBufferBufferedStreamWrite(fos, ibufflen, fbuf);
        return null;
    }

    public byte[] getFbuf() {
        return fbuf;
    }

    public void setFbuf(byte[] fbuf) {
        this.fbuf = fbuf;
    }

    public OutputStream getFos() {
        return fos;
    }

    public void setFos(OutputStream fos) {
        this.fos = fos;
    }

    public int getIbufflen() {
        return ibufflen;
    }

    public void setIbufflen(int ibufflen) {
        this.ibufflen = ibufflen;
    }

    public CustBuffBuffStreamCallableWrite( int ibufflen, byte[] fbuf) {
        this.ibufflen = ibufflen;
        this.fbuf = fbuf;
    }
    

    
}
