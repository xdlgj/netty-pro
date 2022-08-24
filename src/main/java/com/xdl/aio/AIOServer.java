package com.xdl.aio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * AIO同步阻塞IO
 */
public class AIOServer {
    public static void main(String[] args) throws IOException {
        //线程池机制
        /*
        * 实现思路：
        * 1、创建一个线程池子
        * 2、如果客户端连接，就创建一个线程与之通讯（单独写一个方法）
        * */
        // 创建线程池
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
        // 创建一个ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");
        while (true) {
            System.out.println("线程信息:id="+Thread.currentThread().getId()+"名称="+Thread.currentThread().getName());
            //监听，等待客户端连接
            System.out.println("等待连接");
            final Socket accept = serverSocket.accept();
            System.out.println("连接到一个客户端");
            // 创建一个线程与之通信
            newCachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    handler(accept);
                }
            });
        }
    }

    //编写一个handler方法和客户端通信
    public static void handler(Socket socket) {
        try {
            System.out.println("线程信息:id="+Thread.currentThread().getId()+"名称="+Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            // 获取输入流
            InputStream inputStream = socket.getInputStream();
            // 循环读取客户端发送的数据
            while (true) {
                System.out.println("线程信息:id="+Thread.currentThread().getId()+"名称="+Thread.currentThread().getName());
                System.out.println("read...");
                int read = inputStream.read(bytes);
                if (read != -1) {
                    System.out.println(new String(bytes, 0, read));//输出客户端发送的数据
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭与client的连接");
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
