package com.kimura.netty.rpc.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端service调用handler
 */
@Slf4j
public class ServiceClientWriteHandler extends ChannelOutboundHandlerAdapter {
    /**
     * 在active后会调用的一个方法，不是服务端发来的read，是自己调的read
     * @param ctx
     * @throws Exception
     */
    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("method","getByName");
        jsonObject.put("service","com.kimura.netty.rpc.service.impl.UserServiceImpl");
        jsonObject.put("params",new Object[]{"kimura"});
        ctx.writeAndFlush(jsonObject.toJSONString());
        super.read(ctx);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.writeAndFlush("ChannelOutboundHandlerAdapter.write 发来一条消息\r\n");
        super.write(ctx, msg, promise);
    }
}
