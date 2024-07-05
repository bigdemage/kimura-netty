package com.kimura.netty.base.case12;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteOrder;
import java.util.List;

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
public class YbProtocol extends ByteToMessageCodec<String> {

    /**
     * 编码
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        log.info("编码开始");
        //EB90小头字节序--开始
        out.writeBytes(new byte[]{(byte) 0xEB, (byte) 0x90});
        //会话序列号long(八字节小头字节序)
        out.writeBytes(longToLittleEndian(1l));
        //会话源标识	0x00（一个字节）
        out.writeByte(0x00);
        //xml的字节长度 int（四字节小头字节序）
        out.writeBytes(intToLittleEndian(msg.getBytes().length));
        //xml正文
        out.writeBytes(msg.getBytes());
        //结束标志符号	EB90（小头字节序）
        out.writeBytes(new byte[]{(byte) 0xEB, (byte) 0x90});
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        log.info("解码开始");

        //eb90小字节序
        in.readBytes(2);
        //会话序列号
        long serialNuml =getLong(in.readBytes(8));
//        //会话源标识
        in.readByte();
        //xml字节长度
        int xmlLength=getInt(in.readBytes(4));
        byte[] msgByte=new byte[xmlLength];
        in.readBytes(msgByte);
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


    public static void main(String[] args) {
        ByteBuf out= Unpooled.buffer(8);
        out.writeBytes(longToLittleEndian(1l));


        byte[] byteArray = new byte[8];
        out.getBytes(out.readerIndex(), byteArray);

        System.out.println(byteArrayToLong(byteArray));


    }
}
