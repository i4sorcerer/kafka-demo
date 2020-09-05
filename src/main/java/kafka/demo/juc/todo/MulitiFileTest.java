package kafka.demo.juc.todo;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : sk
 */
@Slf4j
public class MulitiFileTest extends Thread {
    private final String msg = "我是一只来自北方的狼，走在无垠的狂野中，凄厉的北风吹过，满满的黄沙略过。我只有咬着冷冷的牙，抱一两声唱响 ";

    private final File file;
    private static AtomicInteger writeLineNum = new AtomicInteger(0);

    private static MultiFileReadWriteTool fileTool = MultiFileReadWriteTool.getInstance(new File("./multy-file-test.txt"));

    public MulitiFileTest(String name, File file) {
        super(name);
        this.file = file;
    }

    @Override
    public void run() {
        log.info("thread " + Thread.currentThread().getName() + " start");

        if (Thread.currentThread().getName().indexOf("read") >= 0) {
            try {
                log.debug(MulitiFileTest.fileTool.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (Thread.currentThread().getName().indexOf("write") >= 0) {
            MulitiFileTest.fileTool.write(msg + writeLineNum.incrementAndGet());
        }
        log.info("thread " + Thread.currentThread() + " end");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String filePath = new String("./multy-file-test.txt");
        File f = new File(filePath);
//        FileWriter fo = new FileWriter(f);
//        for (int line = 0; line < 100; line++) {
//            fo.append("我是测试append-" + line +"\r\n");
//        }
//        fo.close();

        log.info(f.getAbsolutePath());
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                Thread t = new MulitiFileTest("read-" + i, f);
                threads.add(t);
                t.start();
            } else {
                Thread t = new MulitiFileTest("write-" + i, f);
                threads.add(t);
                t.start();
            }
            Thread.sleep(100);
        }
        final AtomicBoolean isAlive = new AtomicBoolean(true);
        for (; ; ) {
            threads.forEach(t -> {
                isAlive.set(t.isAlive());
            });
            if (!isAlive.get())
                break;
            else{
                log.info("等待线程结束...");
                Thread.sleep(1000);
            }
        }
        MultiFileReadWriteTool.getInstance(f).closeAll();
    }

}
