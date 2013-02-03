/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DiskBenchmark;

import BenchCommonUtils.RandomUtils;
import java.io.RandomAccessFile;
import java.util.concurrent.Callable;

/**
 *
 * @author USER
 */
public class RandCallableRead implements MyCallable {

    byte[] fbuf;
    String filePath;
    static int i;
    public RandCallableRead(byte[] fbuf,  String filePath) {
        this.fbuf = fbuf;
        this.filePath= filePath;
    }
    
     public synchronized static int getI() {
        if (i > 2) {
            i = 0;
        } else {
            i++;
        }
        
        return i;
    }

    @Override 
    public Object call() throws Exception {
        long overheadtime = 0;
        long startfilemake = System.nanoTime();
        RandomAccessFile file = new RandomAccessFile(filePath+RandomUtils.getFilepathRead()+ getI(), "r");
        overheadtime = System.nanoTime() - startfilemake;
        overheadtime += DiskBenchUtil.RandFileRead(file, fbuf);
        return overheadtime;
    }
}
