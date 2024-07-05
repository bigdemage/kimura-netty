package com.kimura.netty.base.case12;

import com.kimura.netty.base.case10.LifecycleHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;

import java.nio.charset.Charset;
import java.util.Random;

import static com.kimura.netty.base.util.Constants.LOCAL_PORT;

/**
 * 双向读写服务端-接收到什么就返回什么，客户端也一样
 */
@Slf4j
public class YbServer {
    public static void main(String[] args) {
        new ServerBootstrap()
            .group(new NioEventLoopGroup())
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override protected void initChannel(NioSocketChannel channel) throws Exception {
//                    channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,11,4,0,0));
                    channel.pipeline().addLast(new MessageEncoder());
                    channel.pipeline().addLast(new MessageDecoder());
                    channel.pipeline().addLast(new LifecycleHandler());
                    channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                            String byteBuf = (String) msg;
                            int i= RandomUtils.nextInt(1,2);
                            String byteBuf = "<?xml version='1.0' encoding='UTF-8'?><PatrolDevice><SendCode></SendCode><ReceiveCode></ReceiveCode><Type>251</Type><Command>4</Command></PatrolDevice>";

                            if(i==1){
                                byteBuf = "<?xml version='1.0' encoding='UTF-8'?><PatrolDevice><SendCode></SendCode><ReceiveCode></ReceiveCode><Type>251</Type><Command>3</Command></PatrolDevice>";
                            }
                            log.info("接收信息:{}",byteBuf);
                            //读到什么写回什么
                            channel.writeAndFlush(byteBuf);
                        }
                    });
                }
            }).bind(LOCAL_PORT);
    }
}
