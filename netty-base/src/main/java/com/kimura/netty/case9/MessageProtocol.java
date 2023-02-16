package com.kimura.netty.case9;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * 自定义协议
 * 魔数：用来判定接收的数据是不是无效的数据包，类似暗号
 * 版本号：支持协议升级
 * 序列化算法：消息正文采取什么序列化反序列化方式
 * 指令类型：登录、注册...跟业务相关
 * 请求序号：为双工通信，提供异步能力
 * 正文长度
 * 消息正文
 */
@Slf4j
public class MessageProtocol extends ByteToMessageCodec<UserMessage> {

    /**
     * 编码
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, UserMessage msg, ByteBuf out) throws Exception {
        out.writeBytes(new byte[]{'a','b','c','d'});//魔数
        out.writeByte(1);//版本号
        out.writeByte(2);//序列化算法1-jdk,2-json
        out.writeByte(msg.getType());//指令类型
        out.writeByte(0xff);//请求序号--补齐16个字节
        //获取msg长度
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(bos);
        oos.writeObject(msg);
        byte[] bytes=bos.toByteArray();
        out.writeInt(bytes.length);
        //正文
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int moshu=in.readInt();//魔数
        int version =in.readByte();//版本
        int serialize=in.readByte();//序列化算法
        int type=in.readByte();//指令类型
        Byte serialNum=in.readByte();//请求序号
        int size=in.readInt();//正文长度
        byte[] msgByte=new byte[size];
        in.readBytes(msgByte,0,size);
        ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(msgByte));
        UserMessage msg = (UserMessage) ois.readObject();
        log.info("魔数:{},版本:{},序列化算法:{},指令类型:{},请求序号:{},正文长度:{}",moshu,version,serialize,type,serialNum,size);
        log.info("接收到的对象:{}",msg);
        //信息放入list中传递给下一个handler
        out.add(msg);
    }
}
