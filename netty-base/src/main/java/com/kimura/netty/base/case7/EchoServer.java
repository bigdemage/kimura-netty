package com.kimura.netty.base.case7;

import com.kimura.netty.base.case10.LifecycleHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.nio.charset.Charset;
import lombok.extern.slf4j.Slf4j;

/**
 * 双向读写服务端-接收到什么就返回什么，客户端也一样
 */
@Slf4j
public class EchoServer {
    public static void main(String[] args) {
        new ServerBootstrap()
            .group(new NioEventLoopGroup())
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override protected void initChannel(NioSocketChannel channel) throws Exception {
                    channel.pipeline().addLast(new LifecycleHandler());
                    channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ByteBuf byteBuf = (ByteBuf) msg;
                            log.info("接收信息:{}", byteBuf.toString(Charset.defaultCharset()));
                            //创建byteBuf使用ctx创建
                            ByteBuf response = ctx.alloc().buffer();
                            response.writeBytes(byteBuf);
                            //读到什么写回什么
                            channel.writeAndFlush(response);
                        }
                    });
                }
            }).bind(8055);
    }
}
