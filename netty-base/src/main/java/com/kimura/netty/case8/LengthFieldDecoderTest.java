package com.kimura.netty.case8;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http2.Http2HeadersEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class LengthFieldDecoderTest {
    public static void main(String[] args) {
        EmbeddedChannel embeddedChannel=new EmbeddedChannel(
                /**
                 * 用例详解
                 * https://www.cnblogs.com/java-chen-hao/p/11571229.html
                 *
                 * @param maxFrameLength--最大长度，超过最大长度数据则被丢弃
                 *        the maximum length of the frame.  If the length of the frame is
                 *        greater than this value, {@link TooLongFrameException} will be
                 *        thrown.
                 * @param lengthFieldOffset--长度域的偏移，有些报文不一定是开头就是长度域，所以需要偏移
                 *        the offset of the length field
                 * @param lengthFieldLength--长度域字节数，几个字节表示长度
                 *        the length of the length field
                 * @param lengthAdjustment--数据长度修正。长度域指定的长度可以是header+body的长度，也可以是body的长度，如果表示header+body的整个长度，则需要修正
                 *        the compensation value to add to the value of the length field
                 * @param initialBytesToStrip--跳过的字节数
                 *        the number of first bytes to strip out from the decoded frame
                 */
                new LengthFieldBasedFrameDecoder(1024,0,4,-4,4),
                new LoggingHandler(LogLevel.DEBUG)
        );
        ByteBuf byteBuf= ByteBufAllocator.DEFAULT.buffer();
        //4字节length+实际内容，因为int是4个字节
        send(byteBuf,"hello!kimura");
        send(byteBuf,"hi!");
        embeddedChannel.writeInbound(byteBuf);
    }

    /**
     * 消息发送
     * @param byteBuf
     * @param content
     */
    private static void send(ByteBuf byteBuf,String content){
        byte[] bytes=content.getBytes();
        int size=bytes.length;
        byteBuf.writeInt(size+4);
        byteBuf.writeBytes(bytes);
    }
}
