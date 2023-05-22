package com.kimura.netty.tomcat.pojo;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class ResponseDTO implements Serializable {
    private static final long serialVersionUID = 3891631246579466837L;
    private ChannelHandlerContext ctx;
    private String code = "UTF-8";

    public ResponseDTO(ChannelHandlerContext ctx){
        this.ctx=ctx;
    }

    public void write(String out,HttpResponseStatus status){
        try {
            if (out == null || out.length() == 0) return;
            //设置HTTP及请求头信息
            FullHttpResponse response = new DefaultFullHttpResponse(
                    //设置版本
                    HttpVersion.HTTP_1_1,
                    //设置响应状态码
                    status,
                    //设置输出格式
                    Unpooled.wrappedBuffer(out.getBytes(code)));
            response.headers().set("Content-Type", "text/html;");
            ctx.write(response);
        } catch (UnsupportedEncodingException e) {
            log.error("输出结果异常",e);
        } finally {
            ctx.flush();
            ctx.close();
        }
    }
}
