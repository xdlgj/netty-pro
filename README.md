# BIO问题分析
* 每一个请求都需要创建独立的线程，与对应的客户端进行数据Read，业务处理，数据Write
* 当并发数较大时，需要创建大量线程来处理连接，系统资源占用较大
* 连接建立后，如果当前线程暂时没有数据可读，则线程就阻塞在Read操作上，造成线程资源浪费
# NIO与BIO的
1. BIO以流的方式处理数据，而NIO以块的方式处理数据，块IO的效率比流IO高很多
2. BIO是阻塞的，NIO则是非阻塞的
3. BIO基于字节流和字符流进行操作，而NIO基于Channel（通过）和Buffer（缓冲区）进行操作，数据总是从通道读取到缓冲区中，或者从缓冲区写入到通道中。Selector（选择器）用于监听多个通道的事件（比如：连接请求，数据到达等），因此使用单个线程就可以监听多个客户端通道。
# NIO非阻塞网络编程原理分析图
NIO非阻塞网络编程相关的（Selector、SelectionKey、ServerSocketChannel和SocketChannel）关系梳理图
![selector](./imgs/selector.jpg)
## 原理图说明
1. 当客户端连接时，会通过ServerSocketChannel得到SocketChannel
2. Selector使用select方法进行监听，返回有事件发生的通道的个数
3. 将socketChannel注册到Selector上，register(Selector sel, int ops),一个selector上可以注册多个SocketChannel
4. 注册后返回一个SelectionKey，会和该Selector关联（集合）
5. 进一步得到各个SelectionKey（有事件发生）
6. 再通过SelectionKey的channel方法反向获取SocketChannel
7. 可以通过得到的channel完成业务处理