/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DiskBenchmark;

/**
 *
 * @author USER
 */
public interface MyRunnable extends Runnable{
     
    long getOverheadtime();
    void setOverheadtime(long time);
}
