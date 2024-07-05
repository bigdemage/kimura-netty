package com.kimura.netty.base.case12;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Scanner;

import static com.kimura.netty.base.util.Constants.LOCAL_IP;
import static com.kimura.netty.base.util.Constants.LOCAL_PORT;

/**
 * 双向客户端
 */
@Slf4j
public class YbClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Channel channel = new Bootstrap()
            .group(group)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<NioSocketChannel>() {
                @Override protected void initChannel(NioSocketChannel channel) throws Exception {
                    channel.pipeline()
                            .addLast(new IdleStateHandler(0, 15, 0))
                            .addLast(new YbProtocol())
                            .addLast(new YbClientHandler())
                            .addLast(new HeartbeatHandler());
                }
            }).connect(new InetSocketAddress(LOCAL_IP, LOCAL_PORT)).sync().channel();
        channel.closeFuture().addListener(future -> {
            //优雅关闭
            group.shutdownGracefully();
        });
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine();
                if (line.equals("quit")) {
                    channel.close();
                    break;
                }
                channel.writeAndFlush(line);
            }
        }).start();
    }
}
