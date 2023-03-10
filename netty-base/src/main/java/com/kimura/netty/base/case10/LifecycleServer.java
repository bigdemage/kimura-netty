package com.kimura.netty.base.case10;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 服务端
 */
public class LifecycleServer {

    public static void main(String[] args) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        //核心线程组-
        bootstrap.group(new NioEventLoopGroup());
        //管道
        bootstrap.channel(NioServerSocketChannel.class);
        //处理器-accept事件已经在netty内部处理完成，这个childhander是处理read事件用的
        bootstrap.childHandler(
            new LifecycleHandler()
        );
        bootstrap.bind(8080);
    }
}
