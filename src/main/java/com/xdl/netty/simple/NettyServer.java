package com.xdl.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 创建BossGroup 和WorkerGroup
         * 1、创建两个线程组bossGroup 和 workerGroup
         * 2、bossGroup只是处理连接请求，真正的和客户端业务处理会交给workerGroup完成
         * 3、两个都是无限循环
         * 4、bossGroup 和 workerGroup含有的子线程（NioEventLoop）的个数，默认为：计算机CPU核数 * 2
         * 5、bossGroup接收到连接后，会按照顺序将连接分配给workerGroup中的子线程
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            //使用链式编程来进行设置
            bootstrap.group(bossGroup, workerGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) // 使用NioServerSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 创建一个通道测试对象（匿名对象）
                        // 给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 可以将每个用户的socketChannel存放到集合中，服务器可以根据hashCode找到对应的channel进行发送数据
                            System.out.println("客户端socketChannel hashcode = " + socketChannel.hashCode());
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    }); // 给workerGroup的EventLoop对应的管道设置处理器
            System.out.println("...server is ready");
            // 绑定一个端口并同步，生成一个ChannelFuture对象
            // 启动服务器(并绑定端口)
            ChannelFuture channelFuture = bootstrap.bind(6668).sync();
            channelFuture.addListener(channelFuture1 -> {
                if (channelFuture1.isSuccess()) {
                    System.out.println("服务器监听6668端口成功");
                }
            });
            System.out.println("异步模型~，服务器正在监听端口");
            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
