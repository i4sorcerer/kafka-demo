package kafka.demo.juc.todo;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 写一个例子，实现多线程安全的读写一个文件
 *
 * @author : sk
 */
@Slf4j
public class MultiFileReadWriteTool {
    private static MultiFileReadWriteTool instance = null;
    private File file;
    private FileWriter fileWriter;
    private FileReader fileReader;
    // 到倒数第二行为止的字符数
    private volatile long toLastLineChars;
    private volatile long allChars;

    private final int maxLineChars = 1024;

    private int currentLine;

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock wlock = lock.writeLock();
    private Lock rlock = lock.readLock();

    public static synchronized MultiFileReadWriteTool getInstance(File file) {
        if (instance == null) {
            instance = new MultiFileReadWriteTool(file);
        }
        return instance;
    }

    private MultiFileReadWriteTool(File file) {
        this.file = file;
        toLastLineChars = 0L;
        try {
            fileWriter = new FileWriter(file, true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String line) {
        wlock.lock();
        log.info(wlock.toString());
        try {
            try {
                // 向文件里追加一行内容
                line = line + "\r\n";
                fileWriter.append(line);
                allChars += line.length();
                toLastLineChars = allChars - line.length();

                currentLine++;
                log.info("after appended -> currentLine:" + currentLine + " toLastLineChar:" + toLastLineChars + "[" + line + "]");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
//            try {
//                fileWriter.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            wlock.unlock();
        }
    }

    public String readLine() throws IOException {
        rlock.lock();
        char[] readBuffer = new char[1024];
        try {
            fileReader = new FileReader(file);
            long skippedNums = fileReader.skip(toLastLineChars);
            log.info("read nums:" + String.valueOf(fileReader.read(readBuffer)));
            log.info("skippedNums ->" + skippedNums);
            String lineStr = String.valueOf(readBuffer);
            log.info("thread:" + Thread.currentThread() + "currentLine:" + currentLine + " toLastLineChars[" + toLastLineChars + "] [" + lineStr + "]");
            return lineStr;
        } finally {
            fileReader.close();
            rlock.unlock();
        }
    }

    public boolean closeAll() throws IOException {
        fileReader.close();
        fileWriter.close();
        return true;
    }

}
