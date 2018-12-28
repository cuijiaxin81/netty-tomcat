package com.cui.catalina.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GPResponse {

    private ChannelHandlerContext ctx;
    private HttpRequest r;
    private static Map<Integer,HttpResponseStatus> statusMapping =
            new HashMap<Integer, HttpResponseStatus>();


    static {
        statusMapping.put(200,HttpResponseStatus.OK);
        statusMapping.put(404,HttpResponseStatus.NOT_FOUND);
    }

    public GPResponse(ChannelHandlerContext ctx, HttpRequest r) {
        this.ctx = ctx;
        this.r = r;
    }


    public void write(String out,Integer status) throws UnsupportedEncodingException {
        try {

            if (out == null) {
                out = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
            }

            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                   statusMapping.get(status),
                    Unpooled.wrappedBuffer(out.getBytes("UTF-8")));

            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/json");
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
            response.headers().set(HttpHeaders.Names.EXPIRES, 0);

            if (HttpHeaders.isKeepAlive(r)) {
                response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }

            ctx.write(response);
        }finally {
            ctx.flush();
        }
    }
}
