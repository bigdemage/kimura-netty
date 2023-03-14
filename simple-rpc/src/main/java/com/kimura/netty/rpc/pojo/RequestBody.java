package com.kimura.netty.rpc.pojo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestBody {
    /**
     * 方法名
     */
    private String method;
    /**
     * service全路径
     */
    private String service;
    /**
     * 方法参数
     */
    private Object[] params;
}
