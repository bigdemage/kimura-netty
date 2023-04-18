package com.kimura.netty.rpc.invocation;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;


/**
 * cglib动态代理，使用继承方式
 */
@Slf4j
public class LogInterceptor implements MethodInterceptor {
    /**
     *
     * @param o 要进行增强的对象
     * @param method 拦截的方法
     * @param objects 参数列表，基本数据类型需要传入包装类型
     * @param methodProxy 方法的代理，invokerSuper方法标识对被代理对象方法的调用
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        long start=System.currentTimeMillis();
        Object result=methodProxy.invokeSuper(o,objects);
        log.info("运行时间:{}ms\n运行结果:{}",(System.currentTimeMillis()-start) , JSONObject.toJSONString(result));
        return result;
    }
}
