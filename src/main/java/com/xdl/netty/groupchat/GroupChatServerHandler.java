package com.xdl.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    // 定义一个channel组，管理所有的channel（客户端）
    // GlobalEventExecutor.INSTANCE是全局的事件执行器，是一个单例。因为每一个客户端都需要创建自己的GroupChatServerHandler
    // 但是channelGroup是所有客户端共有的，所以使用静态属性
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * HandlerAdd表示连接建立，一旦连接第一个被执行的方法
     * 将客户端家加入聊天的信息推送给其他在线的客户端
     * 将当前channel加入到channelGroup中
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded");
        Channel channel = ctx.channel();
        // 该方法会将channelGroup中所有的channel遍历，并发送消息
        channelGroup.writeAndFlush("["+dateString()+"][客户端]" + channel.remoteAddress() + " 加入聊天室\n");
        channelGroup.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 一旦触发handlerRemoved方法，会自动执行channelGroup.remove(channel),不需要我们手动执行
        channelGroup.writeAndFlush("["+dateString()+"][客户端]" + channel.remoteAddress() + " 离开聊天室\n");
        System.out.println("channelGroup size = " + channelGroup.size());
    }

    /**
     * 表示channel处于活动状态 提示上线了， 在handlerAdded之后执行
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 上线了~");
    }

    /**
     * 表示channel处于不活动状态，提示离线了，在handlerRemoved之前执行
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 离线了~");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        // 收到消息转发给其他用户
        Channel channel = channelHandlerContext.channel();
        channelGroup.forEach(ch -> {
            if (ch != channel) {
                ch.writeAndFlush("["+dateString()+"][客户端]" + ch.remoteAddress() + " " + msg + "\n");
            } else {
                ch.writeAndFlush("["+dateString()+"][自己]" + ch.remoteAddress() + " " + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    public String dateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
