package kafka.demo.rpc.server;

import com.google.common.io.Closeables;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

/**
 * @author : sk
 */
@Slf4j
public class ProcessorHandler implements Runnable {

    private Object service;
    private Socket socket;
    private Map<String,Object> serviceCache;


    /**
     * 初始化设置服务类和socket
     *
     * @param service
     * @param socker
     */
    public ProcessorHandler(Object service, Socket socker) {
        this.service = service;
        this.socket = socker;
    }
    public ProcessorHandler(Socket socket, Map<String,Object> serviceCache){
        this.socket =socket;
        this.serviceCache=serviceCache;
    }

    @Override
    public void run() {
        log.info("请求服务开始执行->"+ Thread.currentThread());
        ObjectInputStream oinput = null;
        ObjectOutputStream ooutput = null;
        try {
            // 获取socket输入流，获取request
            oinput = new ObjectInputStream(socket.getInputStream());
            RpcRequest rcpRequest = (RpcRequest) oinput.readObject();
            Object response = invoke(rcpRequest);
            // 获取socket的输出流，用来输出结果
            ooutput = new ObjectOutputStream(socket.getOutputStream());
            ooutput.writeObject(response);
            // 刷新输出结果
            ooutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (oinput != null) {
                try {
                    Closeables.close(oinput, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ooutput != null) {
                try {
                    Closeables.close(ooutput, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 利根据request使用反射机制调用服务类，返回结果
     *
     * @param rcpRequest
     * @return
     */
    private Object invoke(RpcRequest rcpRequest) {
        String className = rcpRequest.getClassName();
        String method=rcpRequest.getMethodName();
        String version =rcpRequest.getVersion();

        Object[] paremeters = rcpRequest.getParameters();

        Class<?>[] paremeterTypes= new Class[paremeters.length];
        for(int i=0;i<paremeters.length;i++){
            // 获取每个参数的Class类型
            paremeterTypes[i]=paremeters[i].getClass();
        }
//        Object serviceObj = this.service;
        String serviceName = className+"-"+version;
        Object serviceObj =this.serviceCache.get(serviceName);
        // 通过反射得到Method
        try {
            Method serviceMethod = serviceObj.getClass().getMethod(method,paremeterTypes);
            // 调用服务方法获取调用结果
            Object result = serviceMethod.invoke(serviceObj,paremeters);
            return result;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}

