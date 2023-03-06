package com.kimura.netty.base.case9;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageProtocolTest {

    public static void main(String[] args) throws Exception {
        EmbeddedChannel em=new EmbeddedChannel();
        /**
         * 解码器，防粘包，半包
         * @param maxFrameLength--最大长度，超过最大长度数据则被丢弃
         * @param lengthFieldOffset--长度域的偏移，有些报文不一定是开头就是长度域，所以需要偏移
         * @param lengthFieldLength--长度域字节数，几个字节表示长度
         * @param lengthAdjustment--数据长度修正。长度域指定的长度可以是header+body的长度，也可以是body的长度，如果表示header+body的整个长度，则需要修正
         * @param initialBytesToStrip--跳过的字节数
         */
        em.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024,8,4,0,0));
        em.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
        em.pipeline().addLast(new MessageProtocol());

        UserMessage userMessage=UserMessage.builder().name("kimura").age(20).build();
        ByteBuf byteBuf=ByteBufAllocator.DEFAULT.buffer();
        new MessageProtocol().encode(null,userMessage,byteBuf);

        ByteBuf b1=byteBuf.slice(0,50);
        ByteBuf b2=byteBuf.slice(50,byteBuf.readableBytes()-50);

        em.writeInbound(b1);

    }
}
