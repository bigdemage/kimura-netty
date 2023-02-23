package com.kimura.netty.case11;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Scanner;


/**
 * 心跳检测客户端
 */
@Slf4j
public class HeartBeatClient {

    public static void main(String[] args) {
        try {
            Bootstrap bootstrap=new Bootstrap();
            bootstrap.group(new NioEventLoopGroup());
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new StringEncoder());
                    ch.pipeline().addLast(new StringDecoder());
                    ch.pipeline().addLast(new HeartBeatClientHandler());
                }
            });
            Channel channel=bootstrap.connect(new InetSocketAddress("127.0.0.1",8088)).sync().channel();
            Scanner scanner=new Scanner(System.in);
            while (channel.isActive()){
                String msg=scanner.nextLine();
                channel.writeAndFlush(msg);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
@Slf4j
class HeartBeatClientHandler extends SimpleChannelInboundHandler<String>{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("接收服务端信息-{}",msg);
        if("readyClose".equals(msg)){
            log.info("服务channel关闭，client即将关闭");
            ctx.channel().closeFuture();
        }
    }
}
