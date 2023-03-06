package com.kimura.netty.base.case4;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;

/**
 * netty的future
 */
@Slf4j
public class NettyFutureTest {

    public static void main(String[] args) throws Exception {
        EventLoop eventLoop = new DefaultEventLoop();
        Future future = eventLoop.submit(new Callable<Integer>() {
            @Override public Integer call() throws Exception {
                Thread.sleep(2000);
                return 50;
            }
        });

        log.info("等待中...");
        //同步获取
//        log.info("同步等待获取结果:{}",future.get());
        //异步监听
        future.addListener(new GenericFutureListener<Future<? super Integer>>() {
            @Override public void operationComplete(Future<? super Integer> future) throws Exception {
                log.info("获取结果:{}", future.get());
                eventLoop.shutdownGracefully();
            }
        });
    }
}
