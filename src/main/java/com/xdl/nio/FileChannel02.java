package com.xdl.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 将文件中的数据输出到控制台
 */
public class FileChannel02 {
    public static void main(String[] args) throws IOException {
        File file = new File("hello.txt");
        //创建输入流
        FileInputStream fileInputStream = new FileInputStream(file);
        //通过文件输入流创建FileChannel
        FileChannel channel = fileInputStream.getChannel();
        System.out.println("file.length:" + file.length()); // 一个中文占3个字节
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        channel.read(byteBuffer);
        // 将缓冲区中的数据转换成字节数据，再转换成字符串
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
    }
}
