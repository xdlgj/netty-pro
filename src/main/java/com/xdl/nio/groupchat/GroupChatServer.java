package com.xdl.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.sql.SQLOutput;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {
    //定义属性
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private static final int PORT = 6667;

    // 构造器，初始化工作
    public GroupChatServer() {
        try {
            // 1、创建serverSocketChannel、绑定端口、设置为非阻塞
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(6667));
            serverSocketChannel.configureBlocking(false);

            // 2、创建selector，并将创建serverSocketChannel注册其中
            selector = Selector.open();
            //将serverSocketChannel注册到selector中
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //监听
    public void listen() {
        try {
            // 3、循环监听是否有事件发生
            while (true) {
                int selectNum = selector.selectNow();
                if (selectNum == 0) {
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    // 根据不同的事件，处理对应的业务
                    if (selectionKey.isAcceptable()) { // 触发连接事件
                        // 获取一个socketChannel负责与连接进来的客户端进行通信, 该channel默认为阻塞模式，需要手动设置为非阻塞模式
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        System.out.println("客户端连接成功，获取到一个socketChannel，hashCode=" + socketChannel.hashCode());
                        // 将新创建的socketChannel注册到selector中，关注读事件并关联一个buffer
                        socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                        // todo 需要新客户端上线的消息发送给其他在线的客户端
                        String msg = socketChannel.getRemoteAddress().toString().split(":")[1] + "上线";
                        sendInfoToOtherClient(msg, socketChannel);
                    }
                    if (selectionKey.isReadable()) { // 触发读事件
                        readData(selectionKey);
                    }
                    // 必须移除，不然会存在问题
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //读取客户端数据
    private void readData(SelectionKey selectionKey) {
        SocketChannel channel = null;
        try {
            // 通过selectionKey反相找到与之关联的channel和buffer
            channel = (SocketChannel) selectionKey.channel();
            ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
            //需要清理，不然消息会一直累积
            byteBuffer.clear();
            // 将数据从channel中读取到buffer中
            int count = channel.read(byteBuffer);
            if (count > 0) {
                String msg = new String(byteBuffer.array()).trim();
                System.out.println("from 客户端：" + msg);
                // todo 向其他客户端转发消息
                sendInfoToOtherClient(msg, channel);
            }
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress().toString().substring(1) + " 离线了...");
                //取消注册
                selectionKey.cancel();
                //关闭通道
                channel.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    //转发消息到其他客户端
    private void sendInfoToOtherClient(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中..." + selector.keys().size());
        for (SelectionKey key : selector.keys()) {
            Channel targetChannel = key.channel();
            System.out.println("channel hashcode:" + targetChannel.hashCode());
            //排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                // 转型
                SocketChannel dest = (SocketChannel) targetChannel;
                ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
                dest.write(byteBuffer);
            }
        }
    }
    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
