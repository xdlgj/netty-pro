package com.xdl.nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        // 举例说明Buffer的使用
        // 创建一个Buffer，大小为5，即可以存放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);
        // 向buffer中存放数据，可以不装满，但是不能超出范围java.nio.BufferOverflowException
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }
        /**
         * intBuffer.clear();
         * public final Buffer clear() {
         *         position = 0;
         *         limit = capacity;
         *         mark = -1;
         *         return this;
         *     }
         */
        // 转换buffer（写 => 读）
        intBuffer.flip();
        /**
         * public final Buffer flip() {
         *         limit = position;
         *         position = 0;
         *         mark = -1;
         *         return this;
         *     }
         */
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
