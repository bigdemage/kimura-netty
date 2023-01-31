package com.kimura.netty.case6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import lombok.extern.slf4j.Slf4j;

import static com.kimura.netty.util.ByteLog.log;

/**
 * composite切片合成
 */
@Slf4j
public class CompositeTest {

    public static void main(String[] args) {
        ByteBuf b1= ByteBufAllocator.DEFAULT.buffer();
        b1.writeBytes(new byte[]{1,2,3,4,5});
        ByteBuf b2= ByteBufAllocator.DEFAULT.buffer();
        b2.writeBytes(new byte[]{6,7,8,9,10});

//        ByteBuf all=ByteBufAllocator.DEFAULT.buffer();
//        //引发内存赋值，buf如果过大效率差
//        all.writeBytes(b1).writeBytes(b2);
//        log(all);

        CompositeByteBuf all=ByteBufAllocator.DEFAULT.compositeBuffer();
        //调用add需要使用带true，不然写指针不往下走
        all.addComponents(true,b1,b2);
        log(all);
    }

}
