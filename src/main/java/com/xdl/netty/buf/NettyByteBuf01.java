package com.xdl.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


public class NettyByteBuf01 {
    public static void main(String[] args) {
        /**
         * 里面包含一个byte[10], writerIndex、readerIndex
         * 0 ====== readerIndex ======= writerIndex ======= capacity
         */
        ByteBuf buffer = Unpooled.buffer(10);
        System.out.println(buffer.capacity());
        for (int i = 0; i < buffer.capacity(); i++) {
            // 返回的byteBuf 与 循环外的buffer是一个
            ByteBuf byteBuf = buffer.writeByte(i);
        }
        for (int i = 0; i < buffer.capacity(); i++) {
            // getByte不会改变readerIndex的值
            System.out.println(buffer.getByte(i));
        }
        for (int i = 0; i < buffer.capacity(); i++) {
            // 会改变readerIndex的值
            System.out.println(buffer.readByte());
        }
    }
}
