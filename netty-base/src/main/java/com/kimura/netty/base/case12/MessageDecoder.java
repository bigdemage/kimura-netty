package com.kimura.netty.base.case12;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.kimura.netty.base.case12.LittleEndianConverter.byteArrayToInt;
import static com.kimura.netty.base.case12.LittleEndianConverter.byteArrayToLong;

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
public class MessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //eb90小字节序
        in.readBytes(2);
        //会话序列号
        long serialNuml =getLong(in.readBytes(8));
        //会话源标识
        in.readByte();
        //xml字节长度
        int xmlLength=getInt(in.readBytes(4));
        byte[] msgByte=new byte[xmlLength];
        in.readBytes(msgByte,0,xmlLength);
        String xml=new String(msgByte);
        //放入list传给下一个handler
        out.add(xml);
        //结束标志符号	EB90（小头字节序）
        in.readBytes(2);
    }

    public int getInt(ByteBuf buf){
        byte[] byteArray = new byte[4];
        buf.getBytes(buf.readerIndex(), byteArray);
        return byteArrayToInt(byteArray);
    }

    public long getLong(ByteBuf buf){
        byte[] byteArray = new byte[8];
        buf.getBytes(buf.readerIndex(), byteArray);
        return byteArrayToLong(byteArray);
    }
}
