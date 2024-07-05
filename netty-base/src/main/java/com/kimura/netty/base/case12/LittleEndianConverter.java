package com.kimura.netty.base.case12;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.function.BiFunction;

public class LittleEndianConverter {
    public static void main(String[] args) {
        //
        int intValue = 0xEB90;
        short shortValue = 12345;
        byte byteValue = 123;

        long ii=8l;

        // 将整数转换为小头字节序
        byte[] intBytes = intToLittleEndian(intValue);
        // 将短整数转换为小头字节序
        byte[] shortBytes = shortToLittleEndian(shortValue);
        // 将字节转换为小头字节序
        byte[] byteBytes = byteToLittleEndian(byteValue);
        byte[] longBytes = longToLittleEndian(byteValue);

        System.out.println("整数小头字节序： " + byteArrayToHexString(intBytes));
        System.out.println("短整数小头字节序： " + byteArrayToHexString(shortBytes));
        System.out.println("字节小头字节序： " + byteArrayToHexString(byteBytes));
        System.out.println("长整形小头字节序： " + byteArrayToHexString(longBytes));
    }

    public static int byteArrayToInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN); // 设置字节顺序为小端
        return buffer.getInt();
    }

    public static long byteArrayToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getLong();
    }

    public static byte[] intToLittleEndian(int value) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(value);
        return buffer.array();
    }

    public static byte[] shortToLittleEndian(int value) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort((short) value);
        return buffer.array();
    }

    public static byte[] byteToLittleEndian(byte value) {
        ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(value);
        return buffer.array();
    }

    public static byte[] longToLittleEndian(long value) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putLong(value);
        return buffer.array();
    }

    public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
