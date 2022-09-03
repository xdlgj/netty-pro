package com.xdl.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class NettyByteBuf02 {
    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.copiedBuffer("hello 成都", Charset.forName("utf-8"));
        if (buffer.hasArray()) {
            byte[] content = buffer.array();
            // 将content转成字符串
            System.out.println(new String(content, Charset.forName("utf-8")).trim());
            // buffer = UnpooledByteBufAllocator$InstrumentedUnpooledUnsafeHeapByteBuf(ridx: 0, widx: 12, cap: 24)
            // ridx: 读下标 widx：写下标 cap：容量
            System.out.println("buffer = " + buffer);
            System.out.println(buffer.arrayOffset()); // 0
            System.out.println(buffer.readerIndex()); // 0
            System.out.println(buffer.writerIndex()); // 12
            System.out.println(buffer.capacity()); // 24
            int len = buffer.readableBytes();
            System.out.println("实际数据的长度 = " + len); // 12
        }


    }
}
