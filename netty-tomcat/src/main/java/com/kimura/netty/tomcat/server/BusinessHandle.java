package com.kimura.netty.tomcat.server;

import com.alibaba.fastjson.JSONObject;
import com.kimura.netty.tomcat.pojo.RequestDTO;
import com.kimura.netty.tomcat.pojo.ResponseDTO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class BusinessHandle extends ChannelInboundHandlerAdapter {

    public Map<String,String> service;

    public BusinessHandle(Map<String, String> service) {
        this.service = service;
    }

    public BusinessHandle() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpRequest){
            HttpRequest request = (HttpRequest) msg;
            String url=request.uri();
            log.info("url:{}",url);
            if(url.indexOf("favicon.ico")>0){
                return ;
            }
            //入参
            RequestDTO requestDto = handleUrl(url);
            //校验url是否存在
            Boolean urlExist=verifyUrlExist(requestDto);
            ResponseDTO responseDTO=new ResponseDTO(ctx);
            if(urlExist){
                //执行方法
                String invokeResponse=invokeMethod(requestDto);
                responseDTO.write(invokeResponse,HttpResponseStatus.OK);
            }else{
                responseDTO.write(null,HttpResponseStatus.NOT_FOUND);
            }
        }

    }

    /**
     * url是否存在
     * @param dto
     * @return
     */
    private Boolean verifyUrlExist(RequestDTO dto) {
        String serviceImpl=service.get(dto.getMethod());
        return StringUtils.isNotBlank(serviceImpl);
    }

    /**
     * 调用
     * @param dto
     * @return
     * @throws Exception
     */
    private String invokeMethod(RequestDTO dto) throws Exception {
        String serviceImpl=service.get(dto.getMethod());
        if(StringUtils.isBlank(serviceImpl)) return null;
        Object[] params=dto.getParams();
        Class clazz=Class.forName(serviceImpl);
        Object instance=clazz.newInstance();
        List<Class> list= Arrays.stream(params).map(Object::getClass).collect(Collectors.toList());
        Method method=clazz.getMethod(dto.getMethod(),list.toArray(new Class[list.size()]));
        Object response=method.invoke(instance,params);
        log.info("调用输出结果:{}",response);
        return JSONObject.toJSONString(response);
    }

    /**
     * url逻辑处理
     * @param url
     */
    private RequestDTO handleUrl(String url) {
        RequestDTO dto=new RequestDTO();
        String[] strs=StringUtils.substringAfter(url,"?").split("&");
        List<String> params=new ArrayList<String>();
        for (String str : strs) {
            String[] p=str.split("=");
            params.add(p[1]);
        }
        String methodName =StringUtils.substringBetween(url,"/","?");
        dto.setMethod(methodName);
        dto.setParams(params.toArray());
        return dto;
    }

}
