package com.kimura.netty.rpc.cient;


import com.alibaba.fastjson.JSONObject;
import com.kimura.netty.rpc.handler.ServiceClientReadHandler;
import com.kimura.netty.rpc.handler.ServiceClientWriteHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * rpc客户端
 */
@Slf4j
public class RemoteClient {
    public static void main(String[] args) throws Exception {

        Bootstrap bootstrap=new Bootstrap();
        bootstrap.group(new NioEventLoopGroup());
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new StringEncoder());
                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(new ServiceClientReadHandler());
//                ch.pipeline().addLast(new ServiceClientWriteHandler());
            }
        });
        Channel channel=bootstrap.connect(new InetSocketAddress("localhost",8085)).sync().channel();
        sendMsg(channel);
    }

    private static void sendMsg(Channel channel) {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("method","getByName");
        jsonObject.put("service","com.kimura.netty.rpc.service.impl.UserServiceImpl");
        jsonObject.put("params",new Object[]{"kimura"});
        channel.writeAndFlush(jsonObject.toJSONString());
    }
}
