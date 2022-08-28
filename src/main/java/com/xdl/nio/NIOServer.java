package com.xdl.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws IOException {
        // 获取serverSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 绑定一个端口6666，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 获取selector
        Selector selector = Selector.open();
        // 将serverSocketChannel注册到selector中，关心事件为OP——ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //循环等待客户端连接
        while (true) {
            // 等待1s，如果没有事件发生，继续循环
            if (selector.select(1000) == 0) { // 没有事件发生
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }
            // 有事件发生，就获取到相关的selectionKey集合
            // 1、如果返回>0，表示已经获取到关注的事件
            // 2、selector.selectedKeys()返回关注事件的集合，通过selectionKeys反向获取到通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            // 遍历selectionKeys，使用迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                // 获取到selectionKey
                SelectionKey key = keyIterator.next();
                //根据key对应的通道发生的事件做相应的处理
                if (key.isAcceptable()) { // 触发连接事件
                    //为该客户端生成一个SocketChannel，虽然说accept是阻塞的，但是当客户端触发连接时才执行该行代码，所以会立即执行
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    System.out.println("客户端连接成功，获取到了一个socketChannel，hashCode=" + socketChannel.hashCode());
                    //将该socketChannel注册到selector中,关注读事件，并关联一个buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if (key.isReadable()) { // 触发读事件
                    // 通过key反向找到channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    // 获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer)key.attachment();
                    // 将数据从通道内读取到buffer中
                    channel.read(buffer);
                    System.out.println("from 客户端: " + new String(buffer.array()));
                }
                // 手动从集合中移除当前的selectionKey,防止重复操作
                keyIterator.remove();
            }

        }
    }
}
