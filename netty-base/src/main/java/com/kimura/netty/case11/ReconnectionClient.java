package com.kimura.netty.case11;


import com.sun.org.apache.regexp.internal.RE;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import static com.kimura.netty.util.Constants.LOCAL_IP;
import static com.kimura.netty.util.Constants.LOCAL_PORT;

/**
 * 断线自动重连
 */
@Slf4j
public class ReconnectionClient {
    private Bootstrap bootstrap;
    private String ip;
    private Integer port;

    public ReconnectionClient(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
        init();
    }

    public static void main(String[] args) throws Exception {
        ReconnectionClient client=new ReconnectionClient(LOCAL_IP,LOCAL_PORT);
        client.connect();
    }



    public void init()  {
        bootstrap=new Bootstrap();
        bootstrap.group(new NioEventLoopGroup());
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(new StringEncoder());
                //重连handler
                 ch.pipeline().addLast(new ReconnectionHandler(ReconnectionClient.this));
            }
        });
    }

    /**
     * 连接
     */
    public void connect() throws Exception{
        ChannelFuture channelFuture = this.bootstrap.connect(new InetSocketAddress(ip,port));
        channelFuture.addListener((ChannelFutureListener) future -> {
            if(!future.isSuccess()){
                //开始重连-3秒后重连一次
                //如果想周期性执行，使用scheduleAtFixedRate，想取消的话通过futrue.cancel
                future.channel().eventLoop().schedule(()->{
                    try {
                        connect();
                    } catch (Exception e) {
                        log.error("连接异常",e);
                    }
                },3000, TimeUnit.MILLISECONDS);
            }else{
                log.info("服务端连接成功...干活啦！！");
            }
        });
        //对关闭进行监听
        channelFuture.channel().closeFuture().sync();
    }
}

/**
 * handler处理器
 */
@Slf4j
class ReconnectionHandler extends ChannelInboundHandlerAdapter {

    private ReconnectionClient client;

    public ReconnectionHandler(ReconnectionClient client) {
        this.client = client;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush("hello server");
    }

    /**
     * 连接被关闭
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("开始重连");
        client.connect();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("接收信息--{}",msg);
    }
}
