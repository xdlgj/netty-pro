package com.xdl.netty.inandoutboundhandler.server;

import com.xdl.netty.inandoutboundhandler.handler.ByteToLongDecoder;
import com.xdl.netty.inandoutboundhandler.handler.LongToByteEncoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 入栈的handler进行解码
        pipeline.addLast(new ByteToLongDecoder());
        pipeline.addLast(new LongToByteEncoder());
        // 处理业务的handler，一定要放在编解码器的后面
        pipeline.addLast(new ServerHandler());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
