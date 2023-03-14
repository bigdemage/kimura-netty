package com.kimura.netty.rpc.handler;

import com.alibaba.fastjson.JSONObject;
import com.kimura.netty.rpc.pojo.RequestBody;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 客户端service调用handler
 */
@Slf4j
public class ServiceClientHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("method","getByName");
        jsonObject.put("service","com.kimura.netty.rpc.service.impl.UserServiceImpl");
        jsonObject.put("params",new Object[]{"kimura"});
        ctx.writeAndFlush(msg, promise);
    }
}
