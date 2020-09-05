package kafka.demo.juc;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.LockSupport;

/**
 * AQS 学习demo
 *
 * @author : sk
 */
public class AqsDemo extends AbstractQueuedSynchronizer {
    protected AqsDemo() {
        super();
    }

    @Override
    protected boolean tryAcquire(int arg) {
        return super.tryAcquire(arg);
    }

    @Override
    protected boolean tryRelease(int arg) {
        return super.tryRelease(arg);
    }

    @Override
    protected int tryAcquireShared(int arg) {
        return super.tryAcquireShared(arg);
    }

    @Override
    protected boolean tryReleaseShared(int arg) {
        return super.tryReleaseShared(arg);
    }

    @Override
    protected boolean isHeldExclusively() {
        return super.isHeldExclusively();
    }

    public static void main(String[] args) {
        Thread.currentThread().interrupt();
        LockSupport.unpark(Thread.currentThread());

    }

    // for死循环中可以不用return吗
    public boolean acquireLoop(Object pred, Object node) {
        boolean acquired = false;
        for (; ; ) {
            if (pred == node) {
                return true;
            }
        }
    }

}
