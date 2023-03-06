package com.kimura.netty.base.case1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 服务端
 */
public class HelloServer {

    public static void main(String[] args) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        //核心线程组-
        bootstrap.group(new NioEventLoopGroup());
        //管道
        bootstrap.channel(NioServerSocketChannel.class);
        //处理器-accept事件已经在netty内部处理完成，这个childhander是处理read事件用的
        bootstrap.childHandler(
            new ChannelInitializer<NioSocketChannel>() {
                @Override protected void initChannel(NioSocketChannel channel) throws Exception {
                    //解码
                    channel.pipeline().addLast(new StringDecoder());
                    //自定义事件
                    channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            System.out.println("读取消息：" + msg);
                        }
                    });
                }
            }
        );
        bootstrap.bind(8080);
    }
}
