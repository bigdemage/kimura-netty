package com.kimura.netty.rpc.cient;


import com.kimura.netty.rpc.handler.ServiceClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * rpc客户端
 */
public class RemoteClient {
    public static void main(String[] args) throws Exception {
        Bootstrap bootstrap=new Bootstrap();
        bootstrap.group(new NioEventLoopGroup());
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new StringEncoder());
                ch.pipeline().addLast(new ServiceClientHandler());
            }
        });
        bootstrap.connect(new InetSocketAddress("localhost",8085)).sync();

    }
}
