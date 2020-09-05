package kafka.demo.juc;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 周期任务线程池
 *
 * @author : sk
 */
@Slf4j
public class SchedualedExecutorDemo implements  Runnable{

    private static volatile boolean stopFlag = false;
    // 创建核心线程数是1的延迟任务线程池
    private static ScheduledExecutorService es = Executors.newScheduledThreadPool(3);

    public static void main(String[] args) {
        // 心跳机制check
        for(;;){
            Runnable hbTask = new SchedualedExecutorDemo();
            // 设置执行一次的cmd，在指定延迟之后执行，只执行一次
            es.schedule(hbTask,1000, TimeUnit.MILLISECONDS);
            // 以固定rate或者叫固定的时间间隔，执行任务
            // 设置一个初识delay为1s，没间隔2s执行一次的定时任务，会一直执行
            es.scheduleAtFixedRate(hbTask,1000,2000,TimeUnit.MILLISECONDS);
//            es.scheduleWithFixedDelay();
            stopFlag =true;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(stopFlag) {
                log.info("结束循环");
//                es.shutdownNow();
                break;
            }
        }
    }

    @Override
    public void run() {
        String hbStr = "hread[%s] hbtime[%s]";
        log.info(String.format(hbStr,Thread.currentThread(),LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
    }
}
