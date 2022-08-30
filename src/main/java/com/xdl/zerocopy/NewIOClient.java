package com.xdl.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 7002));
        String fileName = "PMBOK6.pdf";
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();
        long startTime = System.currentTimeMillis();
        /*
         * 在linux下一个transferTo方法就可以完成传输, 底层使用的是零拷贝
         * 在windows下一次调用transferTo只能发送8M数据，需要分段传输文件，而且记住传输的位置
         * */
        final int oneCount = 8 * 1024 * 1024;
        int sendCount = (int) Math.ceil(fileChannel.size() / oneCount);
        long transferCount = 0;
        for (int i = 0; i < sendCount; i++) {
            transferCount += fileChannel.transferTo(i * oneCount, oneCount, socketChannel);
        }

        System.out.println("发送的总字节数 = " + transferCount + " 耗时：" + (System.currentTimeMillis() - startTime));
        fileChannel.close();
    }
}
