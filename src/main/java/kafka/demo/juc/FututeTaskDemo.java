package kafka.demo.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * FutureTaskDemo 实例确认get方法的阻塞
 *
 * @author : sk
 */
@Slf4j
public class FututeTaskDemo extends Thread {
    private static ConcurrentHashMap<String, Future<String>> cachedFutureTask = new ConcurrentHashMap<>(16);

    private String taskName;

    public FututeTaskDemo(String name) {
        this.taskName = name;
    }

    @Override
    public void run() {
        log.info("任务开始 thread->" + Thread.currentThread().getName());
        executeFuturetask(this.taskName);
        log.info("任务结束 thread->" + Thread.currentThread().getName());
    }

    /**
     * 执行futureTask任务
     *
     * @param taskName
     */
    public void executeFuturetask(final String taskName) {
        Future<String> nextTask = cachedFutureTask.get(taskName);
        if (nextTask == null) {
            Callable<String> call = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    // 模拟future任务执行耗时10s
                    if (taskName.equals("name2")) {
                        Thread.sleep(10000);
                    }
                    return taskName;
                }
            };
            FutureTask<String> newTask = new FutureTask<>(call);
            nextTask = cachedFutureTask.putIfAbsent(taskName, newTask);
            // 调用fetureTask的run方法
            if (nextTask == null) {
                nextTask = newTask;
                newTask.run();
            }
        }
        try {
            log.info("异步执行结果thread->" + Thread.currentThread() + " start");
            log.info("异步执行结果thread->" + Thread.currentThread() + ",result->" + nextTask.get());
            log.info("异步执行结果thread->" + Thread.currentThread() + " end");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new FututeTaskDemo("name2").start();
        new FututeTaskDemo("name2").start();
    }

}
