package com.xdl.netty.inandoutboundhandler.client;

import com.xdl.netty.inandoutboundhandler.handler.ByteToLongDecoder;
import com.xdl.netty.inandoutboundhandler.handler.LongToByteEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LongToByteEncoder());
        pipeline.addLast(new ByteToLongDecoder());
        pipeline.addLast(new ClientHandler());
    }
}
