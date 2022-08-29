package com.xdl.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {
    // 定义相关的属性
    private final String HOST = "127.0.0.1"; // 服务器IP
    private final int PORT = 6667; // 服务器端口
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public GroupChatClient() throws IOException {
        selector = Selector.open();
        //连接服务器
        socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", PORT));
        socketChannel.configureBlocking(false);
        //将channel注册到selector
        socketChannel.register(selector, SelectionKey.OP_READ);
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username + "is ok...");
    }

    //向服务器发送消息
    public void sendInfo(String info) {
        info = username + "说：" + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //读取从服务器端回复的消息
    public void readInfo() {
        try {
            int readChannels = selector.select();
            if (readChannels > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if(key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        sc.read(byteBuffer);
                        String msg = new String(byteBuffer.array());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        // 启动客户端
        GroupChatClient chatClient = new GroupChatClient();
        // 启动一个线程
        new Thread() {
            public void run() {
                chatClient.readInfo();
                try {
                    Thread.currentThread().sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        // 发送数据到服务器
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }
    }
}
