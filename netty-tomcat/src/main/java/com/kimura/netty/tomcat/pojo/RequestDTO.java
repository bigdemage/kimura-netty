package com.kimura.netty.tomcat.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDTO implements Serializable {
    private static final long serialVersionUID = 3891631246579466837L;
    private String method;
    private Object[] params;
}
