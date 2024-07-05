package com.kimura.netty.base.case12;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import static com.kimura.netty.base.case12.LittleEndianConverter.*;


/**
 起始标志符	EB90(小头字节序)
 发送会话序列号	long(八字节小头字节序)
 接收会话序列号	long(八字节小头字节序)
 会话源标识	0x00（一个字节）
 xml的字节长度	int（四字节小头字节序）
 交互内容（xml格式）	xml，字符编码为UTF-8
 结束标志符号	EB90（小头字节序）
 */
@Slf4j
public class MessageEncoder extends MessageToByteEncoder<String> {

    /**
     * 编码
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        //EB90小头字节序--开始
        out.writeBytes(shortToLittleEndian(0xEB90));
        //会话序列号long(八字节小头字节序)-每次加1
        out.writeBytes(longToLittleEndian(1l));
        //会话源标识	0x00（一个字节）
        out.writeByte(0x00);
        //xml的字节长度 int（四字节小头字节序）
        out.writeBytes(intToLittleEndian(msg.getBytes().length));
        //xml正文
        out.writeBytes(msg.getBytes());
        //结束标志符号	EB90（小头字节序）
        out.writeBytes(shortToLittleEndian(0xEB90));
    }
}
