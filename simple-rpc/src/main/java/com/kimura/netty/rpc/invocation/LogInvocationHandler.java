package com.kimura.netty.rpc.invocation;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * jdk动态代理
 * @param <T>
 */
@Slf4j
public class LogInvocationHandler<T> implements InvocationHandler {
    /**
     * 被代理对象，实际方法执行者
     */
    T target;
    public LogInvocationHandler(T target){
        this.target=target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("代理类 proxyClass={}，方法名:{}，方法返回类型:{}，接口方法入参:{}",proxy.getClass(),method.getName(),method.getReturnType(),args==null?null: Arrays.toString(args));
        long start=System.currentTimeMillis();
        //调用真实方法
        Object result=method.invoke(target,args);
        log.info("运行时间:{}ms\n运行结果:{}",(System.currentTimeMillis()-start) ,JSONObject.toJSONString(result));
        return result;
    }

}
