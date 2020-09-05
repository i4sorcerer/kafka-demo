package kafka.demo.juc;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : sk
 */
@Slf4j
public class TestMainDemo {

    public static void main(String[] args) throws IOException {
        HashTableDemo();
//        testFileWriter();
    }

    private static void bitMoveDemo() {
        int n = 10;
        log.info("n>>1" + (n >> 1));
        log.info("n<<1" + (n << 1));
    }

    private static void HashTableDemo() {
        final Hashtable<String, String> dict = new Hashtable<>();
        ExecutorService exe = Executors.newFixedThreadPool(10);
        int count = 0;
        while (count < 10) {
            exe.execute(new Runnable() {
                @Override
                public void run() {
                    dict.put("key1", "value1");
                    log.info("get key1" + dict.get("key1"));
                }
            });
            count++;
        }
        exe.shutdown();
    }

    private static void testFileWriter() throws IOException {
        FileReader fr = new FileReader(new File("./multy-file-test.txt"));
        log.info("skipped nums:" + fr.skip(56 * 5));
        char[] bfr = new char[1024];
        log.info("read nums:" + fr.read(bfr));
        log.info("read content[" + String.valueOf(bfr) + "]");

    }

//    public static void main(String[] args) {
//        final BoundedQueue<String> dts = new BoundedQueue<>(10);
//        final AtomicInteger addCount = new AtomicInteger(0);
//        final AtomicInteger removeCount = new AtomicInteger(0);
//        for (int i = 0; i < 12; i++) {
//            addCount.getAndIncrement();
//            new Thread(() -> {
//                dts.add("thread-" + addCount.intValue());
//            }).start();
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        for (int j = 0; j < 8; j++) {
//            removeCount.getAndIncrement();
//            new Thread(() -> {
//                dts.remove();
//            }).start();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
