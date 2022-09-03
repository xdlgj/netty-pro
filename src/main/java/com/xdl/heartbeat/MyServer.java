package com.xdl.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
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
                            /**
                             * 加入一个netty提供的IdleStateHandler
                             * IdleStateHandler是netty提供的处理空闲状态的处理器
                             * readerIdleTime： 多久没有读，就会触发READER_IDLE,
                             * writerIdleTime：多久没有写，就会触发WRITER_IDLE
                             * allIdleTime：多久没有读写， 就会触发ALL_IDLE
                             * unit：时间单位
                             * 文档：
                             *  Triggers an {@link IdleStateEvent} when a {@link Channel} has not performed
                             *  read, write, or both operation for a while.
                             *  当IdleStateHandler触发后，就会传递给管道中下一个handler去处理，通过调用下一个handler的
                             *  userEventTrigger方法，在该方法中处理IdleStateEvent(读空闲、写空闲，读写空闲)
                             */
                            pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                            // 加入一个对空闲检测进一步处理的handler（自定义）
                            pipeline.addLast(new MyServerHandler());
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
