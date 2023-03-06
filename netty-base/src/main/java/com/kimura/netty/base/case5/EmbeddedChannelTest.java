package com.kimura.netty.base.case5;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * netty紫带的channel测试类，不用启动服务
 */
@Slf4j
public class EmbeddedChannelTest {

    public static void main(String[] args) {
        ChannelInboundHandlerAdapter r1 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                log.info("进入r1");
                super.channelRead(ctx, msg);
            }
        };
        ChannelInboundHandlerAdapter r2 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                log.info("进入r2");
                super.channelRead(ctx, msg);
            }
        };
        ChannelOutboundHandlerAdapter w1 = new ChannelOutboundHandlerAdapter() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                log.info("进入w1");
                super.write(ctx, msg, promise);
            }
        };
        ChannelOutboundHandlerAdapter w2 = new ChannelOutboundHandlerAdapter() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                log.info("进入w2");
                super.write(ctx, msg, promise);
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(r1, r2, w1, w2);
//        channel.writeInbound("aa");
        channel.writeOutbound("aa");
    }

}
