package kafka.demo.juc.init;


/**
 * 利用类初始化的方式来确保延迟初始化的安全性
 * 1。 JVM在初始化class的时候会主动加锁
 *
 * @author : sk
 */
public class InstanceFactory {
    private static class MyObjectHolder {
        // 这里的new Obejct操作即便是指令重新排序，也没有问题
        // 因为累的初始化时会加锁
        public static Object instance = new Object();
    }
    public Object gteInstance(){
        return MyObjectHolder.instance;
    }
}
