package com.xdl.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


/**
 * 使用Buffer与Channel将字符串写入到文件中
 */
public class FileChannel01 {
    public static void main(String[] args) throws IOException {
        String str = "hello 徐东林";
        //创建文件输出流 -> channel
        FileOutputStream fileOutputStream = new FileOutputStream("hello.txt");
        //通过fileOutputStream获取对应的FileChannel
        FileChannel channel = fileOutputStream.getChannel();
        //创建缓冲区 buffer
        ByteBuffer buffer = ByteBuffer.allocate(512);
        //将字符串转换成字节数组存放到缓冲区中
        buffer.put(str.getBytes());
        buffer.flip();
        //将缓冲区的数据写入到channel中
        channel.write(buffer);
        // 关闭流
        fileOutputStream.close();
    }
}
