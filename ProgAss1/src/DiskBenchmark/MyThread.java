/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DiskBenchmark;

/** 
 *
 * @author USER
 */
public class MyThread extends Thread{
    private MyRunnable objMyRunnable;


    public MyThread(MyRunnable runnable) {
       super(runnable);
        this.objMyRunnable = runnable;
    }

    public MyRunnable getObjMyRunnable() {
        return objMyRunnable;
    }

    public void setObjMyRunnable(MyRunnable objMyRunnable) {
        this.objMyRunnable = objMyRunnable;
    }
    
}
