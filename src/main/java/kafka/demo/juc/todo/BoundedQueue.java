package kafka.demo.juc.todo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 利用lock和condition来实现一个有界队列的例子
 * 1. 当队列为空时，获取操作将会被阻塞直到有新元素加入
 * 2. 当队列已满时，添加元素操作将会被阻塞直到队列不满为止
 *
 * @author : sk
 */
@Slf4j
public class BoundedQueue<T> {


    Object[] datas;
    private int currentCount, addIndex, removeIndex;
    private Lock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    public BoundedQueue(int size) {
        // 初始化一个固定的数组
        datas = new Object[size];
        // 初始化current
        currentCount = 0;
        addIndex = 0;

    }

    public void logInfo(String msg) {
        log.info("[threadID " + Thread.currentThread() + " [" + msg + "] [" + CollectionUtils.arrayToList(datas) + "]");
    }

    /**
     * 入队列
     *
     * @param ele
     */
    public void add(T ele) {
        try {
            // 先加锁
            lock.lock();
            logInfo("add lock start");
            // 大于0的整数，
            currentCount++;
            while (currentCount > datas.length) {
                try {
                    addIndex = 0;
                    // 等待队列不满的状态通知
                    notFull.await();
                    logInfo("当前队列已满，等待队列不满的状态通知....");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 未满状态正常添加数据，并且通知notEmpty
            datas[addIndex] = ele;
            addIndex++;
            // 唤醒等待删除的线程
            notEmpty.signal();
            logInfo("当前队列notEmpty通知已发出->addEle:" + ele);
        } finally {
            // 最终释放锁
            lock.unlock();
            logInfo("add lock end");
        }
    }

    /**
     * 出队列
     *
     * @return
     */
    public T remove() {
        try {
            // 先加锁
            lock.lock();
            logInfo("remove lock start");
            while (currentCount == 0) {
                // 当前队列未空，等待notempty通知
                try {
                    notEmpty.await();
                    logInfo("当前队列未空，等待notempty通知....");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 队列不为空，正常删除首元素
            Object dt = datas[removeIndex];
            datas[removeIndex] = null;
            if (++removeIndex == datas.length - 1) {
                removeIndex = 0;
            }
            currentCount--;
            // 通知要添加的线程，现在队列是未满状态
            notFull.signal();
            logInfo("当前队列未满通知已发出->delEle:" + dt);
            return (T) dt;
        } finally {
            // 最终释放锁
            lock.unlock();
            logInfo("remove lock end");
        }
    }
}
