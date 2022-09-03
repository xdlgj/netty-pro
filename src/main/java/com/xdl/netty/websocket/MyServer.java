package com.xdl.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyServer {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO)) // 给bossGroup添加日志
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 因为是基于http协议，使用http的编码和解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 是以块方式写，添加ChunkedWriterHandler处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            /*
                            * 1、http数据在传输的过程中是分段的，HttpObjectAggregator就是将多个分段的聚合起来
                            * 2、这就是为什么当浏览器发送大量数据时，就会发出多次http请求
                            * */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /*
                            * 1、对应websocket的数据以帧（frame）的形式进行传递的
                            * 2、可以看到WebSocketFrame下面有6个子类
                            * 3、浏览器请求ws://localhost:7000/hello
                            * 4、WebSocketServerProtocolHandler核心功能是将http协议升级为ws协议（通过101状态码），保持长连接
                            * */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                            pipeline.addLast(new MyTextWebSocketFrameHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
