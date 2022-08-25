package com.xdl.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用一个buffer、两个channel完成文件复制
 */
public class FileChannel03 {
    public static void main(String[] args) throws IOException {
        File file = new File("hello.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileOutputStream fileOutputStream = new FileOutputStream("hello.copy.txt");
        FileChannel inChannel = fileInputStream.getChannel();
        FileChannel outChannel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        while (true) {
            // 注意：必须要清理不然read一直为0会造成死循环
            byteBuffer.clear();
            // 将数据从inChannel读到buffer中
            int read = inChannel.read(byteBuffer);
            System.out.println(read);
            if (read == -1) {
                break;
            }
            // 将数据从buffer中写入到outChannel中
            byteBuffer.flip();
            outChannel.write(byteBuffer);
        }
        fileInputStream.close();
        fileOutputStream.close();
    }
}
