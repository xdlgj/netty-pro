package com.xdl.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MappedByteBuffer可让文件直接再内存（堆外内存）修改，操作系统不需要拷贝一次
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile("hello.txt", "rw");
        //获取对应的通道
        FileChannel channel = file.getChannel();
        /**
         * 参数1：FileChannel.MapMode.READ_WRITE使用的读写模式
         * 参数2：0 表示直接修改的起始位置
         * 参数3：5 表示映射到内存的大小，即hello.txt的多少个字节映射到内存
         * 可以直接修改修改的范围是0-5,不包括5
         * 简单来说：就是从position的位置读取size的长度放到内存中，size可以比源文件的数据总长度大。
         */
        // 实际类型是DirectByteBuffer
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 20);
        mappedByteBuffer.put(0, (byte)'H');
        mappedByteBuffer.put(3, (byte) '9');
        mappedByteBuffer.put(5, (byte) ','); // java.lang.IndexOutOfBoundsException
        mappedByteBuffer.put(6, (byte) '苟'); // 修改中文会出现乱码
        file.close();
    }
}
