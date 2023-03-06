package com.kimura.netty.base.case9;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserMessage implements Serializable {
    private String name;
    private Integer age;
    //消息类型
    @Builder.Default
    private Integer type=1;
}
