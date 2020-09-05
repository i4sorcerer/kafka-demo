package kafka.demo.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * @author : sk
 */
@Slf4j
public class LockSupportDemo {
    public static void main(String[] args) {
        Thread mainThread = Thread.currentThread();
        Thread t1 = new Thread(()->{
            log.info("this is LockSupport test 1");
            log.info("thread interrupted->" + Thread.currentThread().isInterrupted());
            LockSupport.park();
            log.info("this is LockSupport test 2");
        });
        t1.start();

        log.info("this is LockSupport test 3");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LockSupport.unpark(t1);
        log.info("this is LockSupport test 4");
        log.info("this is LockSupport test 5");
//        LockSupport.park();
        log.info("this is LockSupport test 6");

    }
}
