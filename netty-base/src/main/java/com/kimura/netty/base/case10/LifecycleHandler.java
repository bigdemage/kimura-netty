package com.kimura.netty.base.case10;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * channelHandler生命周期
 * 客户端连接：handlerAdded->channelRegistered->channelActive
 * 发送消息：channelRead->channelReadComplete
 * 客户端关闭后：channelInactive->channelUnregistered->handlerRemoved
 * channelActive和channelinactive，表示tcp连接的建立和释放
 */
@Slf4j
public class LifecycleHandler extends ChannelInboundHandlerAdapter {
    /**
     * 当检测到新连接时，调用channel.pipeline.addLast之后的回调
     * 表示在当前的channel中，已经成功添加了一个handler处理器
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("逻辑处理器被添加-handlerAdded");
        super.handlerAdded(ctx);
    }

    /**
     * 表示当前的channel的所有的逻辑处理已经和某个NIO线程建立了绑定关系，类似BIO中，accept到新的连接，创建一个线程来处理这个连接的读写
     * 不过netty是用线程池方式，取一个nioeventloop去绑定channel
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("处理器被注册-channelRegistered");
        super.channelRegistered(ctx);
    }

    /**
     * channel所有业务逻辑链准备完毕，就是channel的pipeline中已经添加完所有的handler，以及绑定好一个nio线程后，连接真正的激活
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("准备就绪-channelActive");
        super.channelActive(ctx);
    }

    /**
     * 客户端向服务端发来数据，就从这个方法读取数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("有数据可读-channelRead");
        super.channelRead(ctx, msg);
    }

    /**
     * 服务端每次读完一次完整的数据后，表示数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("数据读完-channelReadComplete");
        super.channelReadComplete(ctx);
    }

    /**
     * 连接被关闭，在tcp层面已经不再是establish（建立）状态
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channel被关闭-channelInactive");
        super.channelInactive(ctx);
    }

    /**
     * 与channel绑定的线程解除绑定
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channel取消线程nioEventloop绑定-channelUnregistered");
        super.channelUnregistered(ctx);
    }

    /**
     * 所有业务处理器都给移除
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("被移除-handlerRemoved");
        super.handlerRemoved(ctx);
    }
}
