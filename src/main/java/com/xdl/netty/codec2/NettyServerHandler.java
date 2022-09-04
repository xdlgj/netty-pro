package com.xdl.netty.codec2;

import com.xdl.netty.codec.StudentPOJO;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 1、自定义一个Handler需要继承netty规定好的某个HandlerAdapter（规范）
 * 2、这时自定义的Handler才能称为是一个Handler
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {
    /* 读取数据, 重写父类方法的快捷键：Ctrl+o
        1、ChannelHandlerContext ctx：上下文对象，含有管道pipeline，通道channel，地址
        2、Object msg：就是客户端发送的数据 默认为Object类型
     */

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
        // 根据dataType来显示不同的信息
        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();
        if (dataType == MyDataInfo.MyMessage.DataType.StudentType) {
            System.out.println("学生ID=" + msg.getStudent().getId() + " 姓名=" + msg.getStudent().getName());
        } else if (dataType == MyDataInfo.MyMessage.DataType.WorkerType) {
            System.out.println("工人姓名=" + msg.getWorker().getName() + " 年龄=" + msg.getWorker().getAge());
        } else {
            System.out.println("传输的类型不正确");
        }
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
