package com.kimura.netty.case3;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * 同步阻塞式关闭channel和group
 */
@Slf4j
public class ChannelSyncClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group=new NioEventLoopGroup();
        ChannelFuture channelFuture =new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                //这里都是异步连接的，所以需要sync或者监听，不然拿到的channel可能都还没有创建出来
                .connect(new InetSocketAddress("127.0.0.1",8080));
        //同步阻塞，等connet建立
        channelFuture.sync();
        Channel channel=channelFuture.channel();
        log.info("channel:{}",channel);
        Scanner scanner=new Scanner(System.in);
        new Thread(()->{
            while(true){
                String msg=scanner.next();
                if("quit".equals(msg)){
                    channel.close();
                    log.info("关闭channel");
                    break;
                }
                channel.writeAndFlush(msg);
            }
        },"armbar").start();
        //获取closeFuture
        ChannelFuture closeFuture=channel.closeFuture();
        log.info("wait close...");
        //同步等待channel关闭
        closeFuture.sync();
        //彻底关闭jvm，需要把group关闭，优雅关闭
        group.shutdownGracefully();
    }
}
