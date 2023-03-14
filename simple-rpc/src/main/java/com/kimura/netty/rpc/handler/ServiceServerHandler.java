package com.kimura.netty.rpc.handler;

import com.alibaba.fastjson.JSONObject;
import com.kimura.netty.rpc.pojo.RequestBody;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务端service处理handler
 */
@Slf4j
public class ServiceServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("入参是:{}",msg);
        String json= (String) msg;
        RequestBody requestBody= JSONObject.parseObject(json,RequestBody.class);
        String methodName=requestBody.getMethod();
        String service=requestBody.getService();
        Object[] params=requestBody.getParams();
        Class clazz=Class.forName(service);
        Object instance=clazz.newInstance();
        List<Class> list= Arrays.stream(params).map(Object::getClass).collect(Collectors.toList());
        Method method=clazz.getMethod(methodName,list.toArray(new Class[list.size()]));
        Object response=method.invoke(instance,params);
        log.info("调用输出结果:{}",response);
    }
}
