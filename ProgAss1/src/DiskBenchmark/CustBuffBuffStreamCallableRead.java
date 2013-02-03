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
    String filePath;

    public InputStream getFis() {
        return fis;
    }

    public void setFis(InputStream fis) {
        this.fis = fis;
    }

    public CustBuffBuffStreamCallableRead(byte[] fbuf,String filePath) {
        this.fbuf = fbuf;
        this.filePath =filePath;
    }

    public byte[] getFbuf() {
        return fbuf;
    }

    public void setFbuf(byte[] fbuf) {
        this.fbuf = fbuf;
    }

    @Override
    public Object call() throws FileNotFoundException {
        long overheadtime = 0;
        long startfilemake = System.nanoTime();
        fis = new BufferedInputStream(new FileInputStream(filePath+RandomUtils.getFilepathRead()));
        overheadtime = System.nanoTime() - startfilemake;
        overheadtime += DiskBenchUtil.customBufferBufferedStreamRead(fis, fbuf);
        return overheadtime;
    }
}
