package com.xdl.nio;

import java.nio.ByteBuffer;

/**
 * 存入buffer中的数据是什么数据类型，取出时必须是同数据类型
 */
public class NIOByteBufferPutGet {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        buffer.putInt(1);
        buffer.flip();
        // buffer.get(); //无法正确取出数据
        System.out.println(buffer.getInt()); //正确写法
    }
}
