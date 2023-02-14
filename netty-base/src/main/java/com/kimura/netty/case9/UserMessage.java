package com.kimura.netty.case9;

import lombok.Data;

@Data
public class UserMessage {
    private String name;
    private Integer age;
    //消息类型
    private Integer type=1;
}
