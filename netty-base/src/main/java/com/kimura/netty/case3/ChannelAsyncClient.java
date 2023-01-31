package com.kimura.netty.case3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import java.net.InetSocketAddress;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;

/**
 * channel使用异步等待建立连接，异步优雅关闭
 */
@Slf4j
public class ChannelAsyncClient {

    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
            .group(group)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LoggingHandler());
                    ch.pipeline().addLast(new StringEncoder());
                }
            }).connect(new InetSocketAddress("localhost", 8080));
        Scanner scanner = new Scanner(System.in);
        //使用异步监听,因为GenericFutureListener就一个抽象方法，可以用lambda表达式
        channelFuture.addListener((ChannelFutureListener) future -> {
            Channel channel = future.channel();
            new Thread(() -> {
                while (true) {
                    String msg = scanner.next();
                    if ("quit".equals(msg)) {
                        channel.close();
                        log.info("关闭channel");
                        break;
                    }
                    channel.writeAndFlush(msg);
                }
            }, "async-write").start();
            ChannelFuture closeFuture = channel.closeFuture();
            closeFuture.addListener(close -> {
                group.shutdownGracefully();
            });
        });
    }
}
