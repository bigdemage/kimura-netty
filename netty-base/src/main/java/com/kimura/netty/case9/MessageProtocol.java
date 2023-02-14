package com.kimura.netty.case9;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

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


    @Override
    protected void encode(ChannelHandlerContext ctx, UserMessage msg, ByteBuf out) throws Exception {

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

    }
}
