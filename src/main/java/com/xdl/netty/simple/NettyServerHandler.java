package com.xdl.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 1、自定义一个Handler需要继承netty规定好的某个HandlerAdapter（规范）
 * 2、这时自定义的Handler才能称为是一个Handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /* 读取数据, 重写父类方法的快捷键：Ctrl+o
        1、ChannelHandlerContext ctx：上下文对象，含有管道pipeline，通道channel，地址
        2、Object msg：就是客户端发送的数据 默认为Object类型
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //如果这里是一个非常耗时的业务，将会被阻塞在这里。解决办法是：异步执行将处理业务的代码提交到该channel对应的NioEventLoop的taskQueue中
        //ctx.writeAndFlush(Unpooled.copiedBuffer("服务器收到请求，正在处理数据....", CharsetUtil.UTF_8));
        //Thread.sleep(10 * 1000);
        // 解决方案一：用户自定义普通任务taskQueue
        System.out.println(Thread.currentThread().getName());
        // 这里的任务队列并不是一个新的线程,队列中的多个任务会排队执行
        ctx.channel().eventLoop().execute(() -> {
            System.out.println(Thread.currentThread().getName());
            ctx.writeAndFlush(Unpooled.copiedBuffer("服务器收到请求，taskQueue正在处理数据....", CharsetUtil.UTF_8));
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ctx.writeAndFlush(Unpooled.copiedBuffer("服务器收到请求, 完成处理....", CharsetUtil.UTF_8));
        });
        // 方案二: scheduleTaskQueue, 该任务会等待上面的任务执行完才会执行，延迟会先执行
        ctx.channel().eventLoop().schedule(() -> {
            System.out.println(Thread.currentThread().getName());
            ctx.writeAndFlush(Unpooled.copiedBuffer("服务器收到请求，scheduleTaskQueue正在处理数据....", CharsetUtil.UTF_8));
        }, 5, TimeUnit.SECONDS);
        /**
        System.out.println("服务器读取线程 " + Thread.currentThread().getName());
        System.out.println("server ctx =" + ctx);
        System.out.println("看看channel 和 pipeline的关系");
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = ctx.pipeline(); // 本质是一个双向链表，出栈入栈
        // 将msg转成一个ByteBuf，该ByteBuf是Netty提供的，不是NIO的ByteBuffer
         ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送消息是：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址：" + channel.remoteAddress());
         */
    }
    //数据读取完成，不会关心channelRead中异步任务是否执行完成
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓存，并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端～", CharsetUtil.UTF_8));
    }
    //处理异常,一般需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
