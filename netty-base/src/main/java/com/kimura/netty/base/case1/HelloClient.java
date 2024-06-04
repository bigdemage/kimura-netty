package com.kimura.netty.base.case1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import java.net.InetSocketAddress;

import static com.kimura.netty.base.util.Constants.LOCAL_IP;
import static com.kimura.netty.base.util.Constants.LOCAL_PORT;

public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup());
        bootstrap.channel(NioSocketChannel.class);
        //处理器
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override protected void initChannel(NioSocketChannel channel) throws Exception {
                channel.pipeline().addLast(new StringEncoder());
            }
        });
        bootstrap.connect(new InetSocketAddress(LOCAL_IP, LOCAL_PORT))
            .sync()//阻塞同步，直到连接建立
            .channel()//拿到连接channel
            .writeAndFlush("hello");
    }
}
