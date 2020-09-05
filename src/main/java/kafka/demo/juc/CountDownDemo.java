package kafka.demo.juc;

import lombok.extern.slf4j.Slf4j;

/**
 * @author : sk
 */
@Slf4j
public class CountDownDemo {
    public static void main(String[] args) throws InterruptedException {
//        final CountDownLatch countDown = new CountDownLatch(3);
        final CountDownLatchDemo countDown = new CountDownLatchDemo(3);

        new Thread(()->{
            try {
                log.info("t1 start");
                Thread.sleep(1);
                countDown.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 计数器-1操作
        }).start();
        new Thread(()->{
            try {
                log.info("t2 start");
                Thread.sleep(15000);
                countDown.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 计数器-1操作
        }).start();
        new Thread(()->{
            log.info("t3 start");
            countDown.countDown();
            // 计数器-1操作
        }).start();

        // 主线程调用await方法
        log.info("主线程await执行");
        countDown.await();
        log.info("倒计时结束，全部线程执行完毕");
    }
}
