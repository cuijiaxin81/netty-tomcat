package com.cui.catalina.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.apache.log4j.Logger;

public class GPTomcat {

    private static Logger logger = Logger.getLogger(GPTomcat.class);

    public void start(int port) throws Exception{

            //boss线程
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            //work线程
            EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            //Netty服务
            ServerBootstrap server = new ServerBootstrap();
            //链路式编程
            server.group(bossGroup, workGroup)
                    //主线程处理类NioServerSocketChannel
                    .channel(NioServerSocketChannel.class)
                    //子线程的处理 Handler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel client) throws Exception {
                            //服务端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
                            client.pipeline().addLast(new HttpResponseEncoder());
                            //服务端接受到的是httpRequest，所以要用HttpRequestDecoder进行解码
                            client.pipeline().addLast(new HttpRequestDecoder());
                            //最后处理自己的逻辑
                            client.pipeline().addLast(new GPTomcatHandler());
                        }
                    })
                    //配置信息
                    .option(ChannelOption.SO_BACKLOG, 128) //针对主线程的配置
                    .childOption(ChannelOption.SO_KEEPALIVE, true); //针对子线程的配置

            //绑定服务端口
            ChannelFuture future = server.bind(port).sync();

            logger.info("HTTP服务已经启动，监听端口：" + port);

            //开始接收客户端
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        try {
            new GPTomcat().start(8080);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
