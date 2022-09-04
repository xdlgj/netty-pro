package com.xdl.netty.inandoutboundhandler.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ByteToLongDecoder extends ByteToMessageDecoder {
    /**
     * decode会根据接收到的数据，被调用多次，直到确定没有新的元素被添加到list，或者是ByteBuf没有更多的可读字节为止
     * 如果list out 不为空就会将list的内容传递给下一个channelinboundhandler处理，该处理的方法也会被调用多次
     * @param ctx           上下文对象
     * @param in            入栈的buf
     * @param out           List 集合 ，将解码后的数据传给下一个handler处理
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("ByteToLongDecoder 的 decode方法被执行了");
        // 因为long是8个字节, 需要判断有8个字节才能读取
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
