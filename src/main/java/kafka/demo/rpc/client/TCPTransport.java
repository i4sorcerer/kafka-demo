package kafka.demo.rpc.client;

import com.google.common.io.Closeables;
import kafka.demo.rpc.server.RpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author : sk
 */
@Slf4j
public class TCPTransport {
    private String address;

    public TCPTransport(String address) {
        this.address = address;
    }

    /**
     * 新建一个socket
     *
     * @return
     */
    private Socket newSocket() {
        Socket socket = null;

        String[] args = address.split(":");
        try {
            // 新建socker
            socket = new Socket(args[0], Integer.parseInt(args[1]));
            return socket;
        } catch (IOException e) {
            throw new RuntimeException("socket连接创建失败", e);
        }
    }

    /**
     * RPC request远程通信的方法
     *
     * @param rpcRequest
     * @return
     */
    public Object send(RpcRequest rpcRequest) {
        Socket socket = newSocket();
        log.info("socket连接创建成功!");
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        try {
            // 远程获取输出流，写入socket
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(rpcRequest);
            outputStream.flush();
            // 远程获取输入流，结束发送过程
            inputStream = new ObjectInputStream(socket.getInputStream());
            return inputStream.readObject();
        } catch (Exception e) {
            throw new RuntimeException("远程调用发送失败!");
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException("socket连接关闭失败");
                }
            }
            if (inputStream != null) {
                try {
                    Closeables.close(inputStream, true);
                } catch (IOException e) {
                    throw new RuntimeException("输入流关闭失败");
                }
            }
            if (outputStream != null) {
                try {
                    Closeables.close(outputStream, true);
                } catch (IOException e) {
                    throw new RuntimeException("输出流关闭失败");
                }
            }
        }

    }
}