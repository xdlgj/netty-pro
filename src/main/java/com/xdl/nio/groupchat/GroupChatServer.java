package com.xdl.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
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

            // 3、循环监听是否有事件发生
            while (true) {
                int selectNum = selector.selectNow();
                if (selectNum == 0) {
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey selectionKey : selectionKeys) {
                    // 根据不同的事件，处理对应的业务
                    if (selectionKey.isAcceptable()) { // 触发连接事件
                        // 获取一个socketChannel负责与连接进来的客户端进行通信, 该channel默认为阻塞模式，需要手动设置为非阻塞模式
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        System.out.println("客户端连接成功，获取到一个socketChannel，hashCode=" + socketChannel.hashCode());
                        // 将新创建的socketChannel注册到selector中，关注读事件并关联一个buffer
                        socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                        // todo 需要新客户端上线的消息发送给其他在线的客户端
                    }
                    if (selectionKey.isReadable()) { // 触发读事件
                        // 通过selectionKey反相找到与之关联的channel和buffer
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                        // 将数据从channel中读取到buffer中
                        channel.read(byteBuffer);
                        System.out.println("from 客户端：" + new String(byteBuffer.array()));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new GroupChatServer();
    }
}
