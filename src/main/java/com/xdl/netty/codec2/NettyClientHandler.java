package com.xdl.netty.codec2;

import com.xdl.netty.codec.StudentPOJO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Random;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    // 当通道就绪就会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 随机发送Student 或Worker对象 0~2
        int random = new Random().nextInt(3);
        System.out.println(random);
        MyDataInfo.MyMessage myMessage = null;
        if (random == 0) { // Student对象
            MyDataInfo.Student student = MyDataInfo.Student.newBuilder().setId(1).setName("及时雨 宋江").build();
            myMessage = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.StudentType).setStudent(student).build();
        } else { // Worker对象
            MyDataInfo.Worker worker = MyDataInfo.Worker.newBuilder().setName("张三").setAge(20).build();
            myMessage = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.WorkerType).setWorker(worker).build();
        }
        ctx.channel().writeAndFlush(myMessage);
    }

    // 当通道有读取事件时，就会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器回复的消息：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器地址：" + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
