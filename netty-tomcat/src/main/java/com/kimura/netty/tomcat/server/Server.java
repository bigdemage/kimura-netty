package com.kimura.netty.tomcat.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class Server {

    private static final Integer PORT =8080;

    private static final Map<String,String> service=new ConcurrentHashMap<String,String>();

    public static void main(String[] args)  {
        init();
        start();

    }
    private static void init() {
        service.put("getByName","com.kimura.netty.tomcat.service.impl.UserServiceImpl");
        service.put("getByAge","com.kimura.netty.tomcat.service.impl.UserServiceImpl");
    }

    private static void start() {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap().group(boss, work);
        bootstrap.channel(NioServerSocketChannel.class);
        try{
            ChannelFuture future = bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    //入参解码
                    ch.pipeline().addLast(new HttpRequestDecoder());
                    //出参编码
                    ch.pipeline().addLast(new HttpResponseEncoder());
                    //处理逻辑
                    ch.pipeline().addLast(new BusinessHandle(service));
                }
            }).bind(PORT).sync();
            log.info("服务端已启动");
            future.channel().closeFuture().sync();
        }catch (Exception e){
            log.error("异常",e);
        }finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
}
