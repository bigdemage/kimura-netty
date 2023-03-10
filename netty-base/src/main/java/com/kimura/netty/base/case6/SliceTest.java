package com.kimura.netty.base.case6;

import com.kimura.netty.base.util.ByteLog;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

/**
 * 对byteBuf进行切片
 */
@Slf4j
public class SliceTest {

    public static void main(String[] args) {
        //长度为10的直接内存
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
        buf.writeBytes(new byte[] {'1', '2', '3', '4', '5', '6', '7', '8', '9', 'a'});

        //slice切片
        ByteBuf b1 = buf.slice(0, 5);
        b1.retain();
        ByteBuf b2 = buf.slice(5, 5);
        b2.retain();
        ByteLog.log(b1);
        ByteLog.log(b2);

        //证明切片与原来buf是同一块内存
        b1.setByte(0, 'b');
        //因为切片自己有retain，所以buf的release是不影响两个切片的
        buf.release();
        ByteLog.log(b1);

        b1.release();
        ByteLog.log(b1);

    }
}
