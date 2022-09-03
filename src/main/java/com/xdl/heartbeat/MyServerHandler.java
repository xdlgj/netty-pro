package com.xdl.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class MyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     *
     * @param ctx 上下文
     * @param evt 事件
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 将evt向下转型IdleStateEvent
        IdleStateEvent event = (IdleStateEvent) evt;
        String evenType = null;
        switch (event.state()) {
            case READER_IDLE:
                evenType = "读空闲";
                break;
            case WRITER_IDLE:
                evenType = "写空闲";
                break;
            case ALL_IDLE:
                evenType = "读写空闲";
                break;
        }
        System.out.println("eventType = " + evenType);
        System.out.println("服务器做响应的处理.....");
    }
}
