package com.kimura.netty.base.case12;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;


/**
 * 心跳检测
 */
@Slf4j
public class HeartbeatHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            IdleState idleState = idleStateEvent.state();
            if(idleState == IdleState.WRITER_IDLE) {
                log.info("超过60秒没有写数据，发送心跳包");
                ctx.writeAndFlush("超过60秒没有写数据，发送心跳包");
            }

        }
    }

}
