package com.xdl.netty.simple;

import io.netty.util.NettyRuntime;

public class Test {
    public static void main(String[] args) {
        System.out.println("计算机CPU核数：" + NettyRuntime.availableProcessors());
        A a = new A();
        System.out.println(a.name);

    }
}

class A {
    String name;

    public A(String name) {
        System.out.println("有参构造被执行");
        this.name = name;
    }

    public A() {
        // this("xdl"); // 会去调用有参的构造方法
        this.name = "xdl";
    }
}
