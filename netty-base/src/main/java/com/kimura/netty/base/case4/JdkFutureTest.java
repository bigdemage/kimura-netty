package com.kimura.netty.base.case4;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

/**
 * jdk自带
 */
@Slf4j
public class JdkFutureTest {

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        //这里也可以用lambda表达式
        Future future = executor.submit(new Callable<Integer>() {
            @Override public Integer call() throws Exception {
                Thread.sleep(2000);
                return 20;
            }
        });

        log.info("等待中....");
        //阻塞等待，直到获取结果
        log.info("结果为:{}", future.get());
    }
}
