package com.kimura.netty.case1;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventGroupTest {

    public static void main(String[] args) {
        /**
         * 创建时间循环组，里面有很多个eventLoop，每个eventLoop就是一个线程
         * channel只要跟一个eventloop绑定后，以后这个channel的所有事情都是绑定的eventloop处理
         * 一个loop绑定多个channel
         * 有io、事件，普通任务，轮询任务
         */
        EventLoopGroup group=new NioEventLoopGroup(2);

        //获取nioeventLoop
        log.info("group：{}",group.next());
        log.info("group：{}",group.next());
        log.info("group：{}",group.next());

        //执行个普通任务
        group.next().submit(
            ()->{
              log.info("准备好了");
            }
        );

        //执行轮询任务
        group.next().scheduleAtFixedRate(()->{
            log.info("3s执行一次");
        },0,3, TimeUnit.SECONDS);
    }

}
