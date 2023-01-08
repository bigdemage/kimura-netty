package com.kimura.netty.case2;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * 更加细化的eventLoop
 */
@Slf4j
public class EventLoopServer {

    public static void main(String[] args) {

        //处理时间久的handle
        EventLoopGroup defGroup=new DefaultEventLoop();
        //负责接收网络请求
        EventLoopGroup boot=new NioEventLoopGroup();
        //工作eventLoop
    }
}
