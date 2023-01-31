package com.kimura.netty.case6;


import com.kimura.netty.util.ByteLog;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

/**
 * netty的byteBuf
 * 不是byteBuffer，byteBuffer是nio的
 */
@Slf4j
public class ByteBufTest {

    public static void main(String[] args) {
        //使用堆内内存
//        ByteBuf byteBuf= ByteBufAllocator.DEFAULT.buffer(16);
        ByteBuf byteBuf= ByteBufAllocator.DEFAULT.heapBuffer(16);
        //使用直接内存简称对外内存
//        ByteBuf byteBuf= ByteBufAllocator.DEFAULT.directBuffer(16);
        String msg="hello";
        byteBuf.writeBytes(msg.getBytes());
        ByteLog.log(byteBuf);
    }
}
