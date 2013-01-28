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
public class RandCallableRead implements Callable {

    byte[] fbuf;

    public RandCallableRead(byte[] fbuf) {
        this.fbuf = fbuf;
    }

    @Override
    public Object call() throws Exception {
        long overheadtime = 0;
        long startfilemake = System.nanoTime();
        RandomAccessFile file = new RandomAccessFile(RandomUtils.getFilepathRead(), "r");
        overheadtime = System.nanoTime() - startfilemake;

        overheadtime += DiskBenchUtil.RandFileRead(file, fbuf);
        return overheadtime;
    }
}
