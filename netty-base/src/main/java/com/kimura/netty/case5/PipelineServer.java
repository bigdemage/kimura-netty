package com.kimura.netty.case5;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 管道的作用
 */
@Slf4j
public class PipelineServer {

    public static void main(String[] args) {
        new ServerBootstrap()
            .group(new NioEventLoopGroup())
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<NioSocketChannel>() {
                //pipeline管道 head->A1->A2->A3->A4->A5->tail
                //入站操作是先进先出，出站操作是先进后出
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast("A0", new StringDecoder());
                    ch.pipeline().addLast("A1", new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            log.info("A1-接收消息:{}", msg);
                            super.channelRead(ctx, msg);
                        }
                    });
                    ch.pipeline().addLast("A2", new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            User user = new User();
                            user.setName((String) msg);
                            super.channelRead(ctx, user);
                        }
                    });
                    ch.pipeline().addLast("B1", new ChannelOutboundHandlerAdapter() {
                        @Override
                        public void write(ChannelHandlerContext ctx, Object msg,
                            ChannelPromise promise) throws Exception {
                            log.info("当前节点B1--{}", msg);
                            super.write(ctx, msg, promise);
                        }
                    });
                    ch.pipeline().addLast("B2", new ChannelOutboundHandlerAdapter() {
                        @Override
                        public void write(ChannelHandlerContext ctx, Object msg,
                            ChannelPromise promise) throws Exception {
                            log.info("当前节点B2--{}", msg);
                            super.write(ctx, msg, promise);
                        }
                    });
                    ch.pipeline().addLast("A3", new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            log.info("A3-接收消息:{}", msg);
                            //write写入，然后会从尾巴开始往回找outbound
                            ch.writeAndFlush("马里奥赛车");
                            //如果是使用ChannelHandlerContext形式发送信息，下面的出站操作是接收不到的，因为ctx的write后会从当前处理器向前去找outbound
                            ctx.writeAndFlush(msg);
                            super.channelRead(ctx, msg);
                        }
                    });
                    ch.pipeline().addLast("A4", new ChannelOutboundHandlerAdapter() {
                        @Override
                        public void write(ChannelHandlerContext ctx, Object msg,
                            ChannelPromise promise) throws Exception {
                            log.info("当前节点A4--{}", msg);
                            super.write(ctx, msg, promise);
                        }
                    });
                    ch.pipeline().addLast("A5", new ChannelOutboundHandlerAdapter() {
                        @Override
                        public void write(ChannelHandlerContext ctx, Object msg,
                            ChannelPromise promise) throws Exception {
                            log.info("当前节点A5");
                            super.write(ctx, msg, promise);
                        }
                    });
                }
            }).bind(8080);
    }
}

class User {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
            "name='" + name + '\'' +
            '}';
    }
}
