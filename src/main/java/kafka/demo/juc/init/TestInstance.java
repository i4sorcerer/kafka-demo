package kafka.demo.juc.init;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : sk
 */
public class TestInstance {
    public static void main(String[] args) {
        ExecutorService exec =  Executors.newFixedThreadPool(3);
        exec.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(DoubleCheckLock.getInstance() +" thread1:" + Thread.currentThread());
            }
        });
        exec.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(DoubleCheckLock.getInstance() +" thread2:" + Thread.currentThread());
            }
        });
        exec.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(DoubleCheckLock.getInstance() +" thread3:" + Thread.currentThread());
            }
        });
        exec.shutdown();
    }
}
