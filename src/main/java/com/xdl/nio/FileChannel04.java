package com.xdl.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用transFrom实现文件拷贝
 */
public class FileChannel04 {
    public static void main(String[] args) throws IOException {
        File file = new File("img.jpeg");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileOutputStream fileOutputStream = new FileOutputStream("img.copy.jpeg");
        FileChannel inChannel = fileInputStream.getChannel();
        FileChannel outChannel = fileOutputStream.getChannel();
        // 将inChannel的数据拷贝到outChannel
        outChannel.transferFrom(inChannel, 0, file.length());
        fileInputStream.close();
        fileOutputStream.close();
    }
}
