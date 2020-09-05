package kafka.demo.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author : sk
 */
@Slf4j
public class ExecutorsTestDemo implements Runnable {

//    private static ExecutorService es = Executors.newFixedThreadPool(3);
    private static ExecutorService es = new ThreadPoolExecutor(3, 6,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingDeque<Runnable>());

    public static void main(String[] args) {
        for (int i=0;i<20;i++){
            // 启动100个任务，但线程池中只有3个线程。
            // 3个线程去处理100个任务
//            es.execute(new ExecutorsTestDemo());
            // 将任务提交到线程池，并获得执行的结果
//            Runnable task =new ExecutorsTestDemo();
//            Callable<String> task = new ExecutorsTestDemo();
            Callable<String> task2 = Executors.callable(new ExecutorsTestDemo(), "测试结果");
            Future<?> result = (FutureTask<String>)es.submit(task2);
            try {
                log.info("执行结果 task->" +task2 + " result->" +result.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        log.info("线程池shutdown");
        es.shutdown();
    }
    @Override
    public void run() {
        log.info("任务执行开始->" +Thread.currentThread());
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("任务执行结束->" +Thread.currentThread());
    }

    public String call() throws Exception {
        log.info("任务执行开始->" +Thread.currentThread());
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("任务执行结束->" +Thread.currentThread());
        return "执行任务成功thread->" +Thread.currentThread();
    }
}
