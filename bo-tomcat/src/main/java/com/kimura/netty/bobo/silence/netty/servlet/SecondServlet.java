package com.kimura.netty.bobo.silence.netty.servlet;

import com.kimura.netty.bobo.silence.netty.http.BoRequest;
import com.kimura.netty.bobo.silence.netty.http.BoResponse;
import com.kimura.netty.bobo.silence.netty.http.BoServlet;

public class SecondServlet extends BoServlet {
    protected void doPost(BoRequest request, BoResponse response) throws Exception {
        response.write("This is SecondServlet");
    }

    protected void doGet(BoRequest request, BoResponse response) throws Exception {
        doPost(request,response);
    }
}
