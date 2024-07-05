package com.kimura.netty.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class TestCode {

    public static void main(String[] args) throws IOException {
        String msg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><PatrolDevice><SendCode>Client01</SendCode><ReceiveCode>Server01</ReceiveCode><Type>2</Type><Code/><Command/><Time>2022-01-01 12:02:34</Time><Items><Item patroldevice_name =\"500kV机器人\" patroldevice_code=\"Robot01\" time=\"2022-01-01 12:02:12\" type =\"1\" value=“0.4” value_unit=“0.4m/s” unit=”m/s”/><Item patroldevice_name =\"500kV机器人\" patroldevice_code =\"Robot01\" time=\"2022-01-01 12:02:12\" type =\"2\" value=“942.5” value_unit=“942.5m” unit=”m”/><Item patroldevice_name =\"500kV机器人\" patroldevice_code =\"Robot01\" time=\"2022-01-01 12:02:12\" type =\"3\" value=“90.5” value_unit=“90.5%” unit=”%”/></Items></PatrolDevice >";
        // 获得序列化后的msg
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(msg);
        byte[] bytes = bos.toByteArray();

        // 获得并设置正文长度 长度用4个字节标识
        System.out.println(bytes.length);
        System.out.println(msg.length());
    }


}
