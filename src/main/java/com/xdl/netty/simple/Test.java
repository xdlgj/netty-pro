package com.xdl.netty.simple;

import io.netty.util.NettyRuntime;

public class Test {
    public static void main(String[] args) {
        System.out.println("计算机CPU核数：" + NettyRuntime.availableProcessors());
    }
}
