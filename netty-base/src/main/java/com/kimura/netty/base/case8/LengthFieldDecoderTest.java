package com.kimura.netty.base.case8;

import com.kimura.netty.base.case12.YbProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http2.Http2HeadersEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import static com.kimura.netty.base.case12.LittleEndianConverter.longToLittleEndian;


@Slf4j
public class LengthFieldDecoderTest {

    public static final String head="zk-";
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
                 * 如果报文 length = 10，content真正的长度为10，后续报文 = content = 10，那 lengthAdjustment = 0，报文长度无修修正。
                 * 如果报文 length = 15，content真正的长度为10（说明length代表整个报文的长度，也就是 length + header+ content ），后续报文 = content = 10，后续报文和length（15）不等，所以报文长度需要修正，lengthAdjustment = -5。

                 * @param initialBytesToStrip--跳过的字节数
                 *        the number of first bytes to strip out from the decoded frame
                 */
                new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,11,4,2,0),
                new LoggingHandler(LogLevel.DEBUG)
//                new YbProtocol()
        );
        ByteBuf byteBuf= ByteBufAllocator.DEFAULT.buffer();
        //4字节length+实际内容，因为int是4个字节
        send(byteBuf,"hello!kimura");
        send(byteBuf,"hi!");
        send(byteBuf,"sssssssssssssssssssssssssssssss!");
        embeddedChannel.writeInbound(byteBuf);
    }

    /**
     * 消息发送
     * @param byteBuf
     * @param content
     */
    private static void send(ByteBuf byteBuf,String content){
        byteBuf.writeBytes(new byte[]{(byte) 0xEB, (byte) 0x90});
        //会话序列号long(八字节小头字节序)
        byteBuf.writeBytes(longToLittleEndian(1l));
        //会话源标识	0x00（一个字节）
        byteBuf.writeByte(0x00);
        byte[] bytes=content.getBytes();
        int size=bytes.length;
        byteBuf.writeInt(size);
        log.info("报文长度：{}",size);
        byteBuf.writeBytes(bytes);
        byteBuf.writeBytes(new byte[]{(byte) 0xEB, (byte) 0x90});

    }
}
