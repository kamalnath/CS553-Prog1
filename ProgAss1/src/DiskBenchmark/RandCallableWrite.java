/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DiskBenchmark;

import BenchCommonUtils.RandomUtils;
import java.io.RandomAccessFile;

/**
 *
 * @author USER
 */
public class RandCallableWrite implements MyCallable {

    private byte[] fbuf;
    String filePath;

    public RandCallableWrite(byte[] fbuf,String filePath) {
        this.fbuf = fbuf;
        this.filePath= filePath;
    }

    @Override
    public Object call() throws Exception {
        long overheadtime = 0;
        long startfilemake = System.nanoTime();
         RandomAccessFile file = new RandomAccessFile(filePath+RandomUtils.getRandFileName(), "rw");
        overheadtime = System.nanoTime() - startfilemake;
       
        overheadtime += DiskBenchUtil.RandFileWrite(file, fbuf);
        return overheadtime;
    }
}
