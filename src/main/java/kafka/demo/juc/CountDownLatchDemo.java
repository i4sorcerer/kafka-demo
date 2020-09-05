package kafka.demo.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 手写CountDownLatch的实现
 *
 * @author : sk
 */
@Slf4j
public class CountDownLatchDemo {

    private final Sync sync;


    public CountDownLatchDemo(int count) {
        sync =new Sync(count);
    }

    public void countDown(){
        sync.releaseShared(1);
    }
    public  void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    /**
     * 私有静态内部helper类，AQS的实现类
     */
    private static final class Sync extends AbstractQueuedSynchronizer{
        public Sync(int count){
            setState(count);
        }
        public int getCount(){return getState();}


        /**
         * 尝试获得所得方法
         *
         * @param arg
         * @return
         */
        @Override
        protected int tryAcquireShared(int arg) {
            // 如果当前计数器为0，则表示倒计时结束，主线程可以被唤醒继续往下走
            // 否则的话，表示计数器还未结束，主线程需要wait
            return (getState()==0) ? 1 : -1;
    }

    @Override
        protected boolean tryReleaseShared(int arg) {
            // 计数器递减，当计数器为0时，signal
            for (;;){
                int c= getState();
                if (c==0) return false;
                int nextC = c-arg;
                if (compareAndSetState(c,nextC)){
                    log.info("tryReleaseShared设置成功，currentC:"+c + ",nextC:" + nextC);
                    // 这里为什么不是直接返回true，而是return nextC==0?????
                    return true;
                }
            }
        }
    }
}



