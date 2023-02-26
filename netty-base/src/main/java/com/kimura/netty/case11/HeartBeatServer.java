package com.kimura.netty.case11;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import static com.kimura.netty.util.Constants.LOCAL_PORT;


/**
 * 心跳检测机制
 * netty是基于tcp协议开发的，在tcp协议实现中也停供了keepalive报文用来检测对端是否可用
 * tcp keepAlive是用于检测链接的死活，而心跳检测机制是需要检测连接可不可用，检测双方的通讯状态是否存活
 * 例如某台服务器因为某些原因导致负载过高，cpu100%，双方已经没法通讯了，但此时keepAlive是true
 * 这就是典型的连接活着而业务提供方已经死掉的状态，对客户端而言，这时应该断连去连接其他服务器，而不是连接一台发不出消息的服务器
 */
@Slf4j
public class HeartBeatServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            /**
             * SO_BACKLOG对应tcp/ip协议listen函数中的backlog参数，用来初始化服务端可连接的队列，服务端处理客户端连接请求是顺序处理的，多个连接过来时是不能同时
             * 处理的，需要放到队列里，1024就是设置队列的长度
             */
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024)
                      //tcp级别的心跳检测
                     .childOption(ChannelOption.SO_KEEPALIVE, true);
            //childHandler是处理工作线程的，handler是处理boss链接类线程，netty已经处理好
            ChannelFuture future = bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new StringDecoder());
                    ch.pipeline().addLast(new StringEncoder());
                    /**
                     * 心跳检测的handler，单位秒
                     * @param readerIdleTimeSeconds--当在指定时间内没有从channel读取到数据时，会出发READER_IDLE的IdelStateEvent事件
                     * @param writerIdleTimeSeconds--当在指定时间内没有数据写入到channel时，会出发WRITER_IDLE的IdleStateEvent事件
                     * @param allIdleTimeSeconds--指定时间内没有读写操作时，出发一个ALL_IDLE的IdleStateEvent事件
                     */
                    ch.pipeline().addLast(new IdleStateHandler(5, 0, 0));
                    ch.pipeline().addLast(new HeartBeatServerHandler());
                }
            }).bind(LOCAL_PORT).sync();
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}

/**
 * SimpleChannelInboundHandler是继承ChannelInboundHandlerAdapter
 * 可以使用泛型来确定入站消息的类型
 */
class HeartBeatServerHandler extends SimpleChannelInboundHandler<String> {

    //读取空闲次数
    int readIdleTimes = 0;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(String.format("消息接收-%s", msg));
        if ("ping".equals(msg)) {
            ctx.channel().writeAndFlush("pong");
        } else {
            System.out.println("业务处理中....");
        }
    }


    /**
     * 事件触发
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
        String eventType = null;
        switch (idleStateEvent.state()) {
            case READER_IDLE:
                eventType = "读空闲";
                readIdleTimes++; // 读空闲的计数加1
                break;
            case WRITER_IDLE:
                eventType = "写空闲";
                // 不处理
                break;
            case ALL_IDLE:
                eventType = "读写空闲";
                // 不处理
                break;
        }
        System.out.println("超时事件为---" + eventType);
        if (readIdleTimes > 3) {
            System.out.println("已超时3次，关闭链接，释放资源");
            ctx.channel().writeAndFlush("readyClose");
            ctx.channel().close();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println(String.format("%s-已连接", ctx.channel().remoteAddress()));
    }
}

