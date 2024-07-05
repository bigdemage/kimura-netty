package com.kimura.netty.base.case12;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * @Description: netty 客户端处理器
 */
@Slf4j
// 标记该类实例可以被多个 channel 共享
@ChannelHandler.Sharable
public class YbClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 接收消息
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        log.info("接收到来自服务端的消息:{}",msg);
        // 释放消息
        ReferenceCountUtil.release(msg);
    }

    /**
     * 和服务器建立连接时触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端连接到服务器!!!");
        String msg="连接上了";
        ctx.channel().writeAndFlush(msg);
    }

    /**
     * 有异常时触发
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("客户端异常", cause);
        super.exceptionCaught(ctx, cause);
    }
}
