package com.kimura.netty.base.case2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

import static com.kimura.netty.base.util.Constants.LOCAL_PORT;

/**
 * 更加细化的eventLoop
 */
@Slf4j
public class EventLoopServer {

    public static void main(String[] args) throws Exception {

        /**
         * 当需要处理的事务需要比较长的时间，害怕耽误下一个handler处理，可以专门开一个group去处理
         */
        EventLoopGroup defGroup = new DefaultEventLoop();
        //负责接收网络请求
        EventLoopGroup boot = new NioEventLoopGroup();
        //工作eventLoop
        EventLoopGroup work = new NioEventLoopGroup();

        new ServerBootstrap()
            .group(boot, work)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel nioSocketChannel) {
                    nioSocketChannel.pipeline().addLast(new StringDecoder());
                    nioSocketChannel.pipeline().addLast(defGroup, new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            log.info("第一手-接收消息:{}", msg);
                            //调用后把上下文和msg传到下一个处理
                            super.channelRead(ctx, msg);
                        }
                    }).addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) {
                            log.info("第二手-接收消息:{}", msg);
                        }
                    });
                }
            })
            .bind(LOCAL_PORT).sync();

    }
}
