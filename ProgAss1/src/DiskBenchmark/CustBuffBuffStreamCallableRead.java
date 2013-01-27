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
import java.io.OutputStream;
import java.util.concurrent.Callable;

/**
 *
 * @author kamalnath_ng
 */
public class CustBuffBuffStreamCallableRead implements Callable {

    InputStream fis;
    byte[] fbuf;

    public InputStream getFis() {
        return fis;
    }

    public void setFis(InputStream fis) {
        this.fis = fis;
    } 

    public CustBuffBuffStreamCallableRead(byte[] fbuf) {
        this.fbuf = fbuf;
    }

    public byte[] getFbuf() {
        return fbuf;
    }

    public void setFbuf(byte[] fbuf) {
        this.fbuf = fbuf;
    }

    @Override
    public Object call() throws FileNotFoundException {
        fis = new BufferedInputStream(new FileInputStream(RandomUtils.getFilepathRead()));
        DiskBenchUtil.customBufferBufferedStreamRead(fis, fbuf);
        return null;
    }
}
