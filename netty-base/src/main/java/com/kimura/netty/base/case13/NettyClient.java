package com.kimura.netty.base.case13;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class NettyClient {
    public static final String HOST = "localhost";
    public static final int PORT = 8085;
    public static final int RECONNECT_DELAY = 5; // 重连间隔时间（秒）

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline().addLast(new IdleStateHandler(0, 0, RECONNECT_DELAY / 1000));
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });

        connect(bootstrap);
    }

    private static void connect(Bootstrap bootstrap) {
        bootstrap.connect(HOST, PORT).addListener((ChannelFuture future) -> {
            if (future.isSuccess()) {
                System.out.println("连接成功");
            } else {
                System.out.println("连接失败，尝试重新连接");
                ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
                scheduledExecutorService.schedule(() -> {
                    System.out.println("尝试重新连接");
                    connect(bootstrap);
                }, RECONNECT_DELAY, TimeUnit.SECONDS);
            }
        });
    }

    private static class ClientHandler extends SimpleChannelInboundHandler<Object> {
        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            System.out.println("客户端连接成功");
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            System.out.println("客户端连接断开，尝试重新连接");
            connect(new Bootstrap());
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        }
    }
}
