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


/**
 * 心跳检测机制
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
                    ch.pipeline().addLast(new IdleStateHandler(3, 0, 0));
                    ch.pipeline().addLast(new HeartBeatServerHandler());
                }
            }).bind(8088).sync();
            future.channel().closeFuture().sync();
        }finally {
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
    int readIdleTimes=0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(String.format("消息接收-%s",msg));
        if("ping".equals(msg)){
            ctx.channel().writeAndFlush("pong");
        }else{
            System.out.println("业务处理中....");
        }
    }


    /**
     * 事件触发
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent idleStateEvent= (IdleStateEvent) evt;
        String eventType = null;
        switch (idleStateEvent.state()){
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
        System.out.println("超时事件为---"+eventType);
        if(readIdleTimes>3){
            System.out.println("已超时3次，关闭链接，释放资源");
            ctx.channel().writeAndFlush("readyClose");
            ctx.channel().close();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)  {
        System.out.println(String.format("%s-已连接",ctx.channel().remoteAddress()));
    }
}

