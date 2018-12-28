package com.cui.catalina.servlet;

import com.cui.catalina.http.GPRequest;
import com.cui.catalina.http.GPResponse;
import com.cui.catalina.netty.server.GPServlet;

import java.io.UnsupportedEncodingException;

public class MyServlet extends GPServlet {



    @Override
    public void doGet(GPRequest request, GPResponse response) {
        doPost(request,response);
    }

    @Override
    public void doPost(GPRequest request, GPResponse response) {
        String param = "name";
        String str = request.getParameter(param);
        try {
            response.write(param + ":" + str,200);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
