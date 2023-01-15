package com.kimura.netty.case4;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

/**
 * netty自带promise
 */
@Slf4j
public class NettyPromiseTest {

    public static void main(String[] args) throws Exception {
        EventLoop eventLoop=new DefaultEventLoop();

        Promise<Integer> promise=new DefaultPromise<>(eventLoop);

        new Thread(()->{
            try {
                log.info("计算中....");
                Thread.sleep(2000);
                int i =1/0;
                promise.setSuccess(100);
            } catch (InterruptedException e) {
                promise.setFailure(e);
                throw new RuntimeException(e);
            }
        },"aaaaa").start();

        log.info("开始等待...");

        log.info("结果:{}",promise.get());

    }
}
