package kafka.demo.juc.init;

/**
 * 1. 双重check
 * 2. 单例模式实现方式之一
 * 3. volatile的使用
 *
 * @author : sk
 */
public class DoubleCheckLock {
    private volatile static DoubleCheckLock instance;
    public static DoubleCheckLock getInstance(){
        if (instance == null){
            synchronized (DoubleCheckLock.class){
                if (instance==null){
                    instance = new DoubleCheckLock();
                }
            }
        }
        return instance;
    }
    private DoubleCheckLock() {}
}

