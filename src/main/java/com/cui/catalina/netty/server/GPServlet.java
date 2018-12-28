package com.cui.catalina.netty.server;

import com.cui.catalina.http.GPRequest;
import com.cui.catalina.http.GPResponse;

public abstract class GPServlet {

    public abstract void doGet(GPRequest request, GPResponse response);
    public abstract void doPost(GPRequest request,GPResponse response);

}
