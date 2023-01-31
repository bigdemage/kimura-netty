package com.kimura.netty.case7;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;

/**
 * 双向客户端
 */
@Slf4j
public class EchoClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Channel channel = new Bootstrap()
            .group(group)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<NioSocketChannel>() {
                @Override protected void initChannel(NioSocketChannel channel) throws Exception {
                    channel.pipeline().addLast(new StringEncoder());
                    channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ByteBuf byteBuf = (ByteBuf) msg;
                            log.info("接收服务端信息:{}", byteBuf.toString(Charset.defaultCharset()));
                        }
                    });
                }
            }).connect(new InetSocketAddress("127.0.0.1", 8080)).sync().channel();
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
