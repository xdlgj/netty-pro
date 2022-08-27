package com.xdl.nio;

import java.nio.ByteBuffer;

public class ReadOnlyBuffer {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        for (int i = 0; i < 64; i++) {
            buffer.put((byte) i);
        }
        //buffer.flip();
        // 复制buffer，并把新的buffer设置为只读,//java.nio.ReadOnlyBufferException
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
        readOnlyBuffer.flip(); // 如果复制之前的buffer进行了flip，此处就可以省略
        System.out.println(readOnlyBuffer.getClass()); //java.nio.ReadOnlyBufferException
        while (readOnlyBuffer.hasRemaining()) {
            System.out.println(readOnlyBuffer.get());
        }
    }
}
