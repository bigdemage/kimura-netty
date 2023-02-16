package com.kimura.netty.case10;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * LifecycleHandler测试
 */
@Slf4j
public class LifecycleHandlerTest {
    public static void main(String[] args) {

        EmbeddedChannel ec = new EmbeddedChannel();
        ec.pipeline().addLast(new LifecycleHandler());
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeByte(1);
        ec.writeInbound(byteBuf);
    }
}
