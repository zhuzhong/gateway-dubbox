package com.z.nettyserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.DispatcherServlet;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyHttpServer {

    private int port;
    private static Logger log = LoggerFactory.getLogger(NettyHttpServer.class);
    private DispatcherServlet servlet;

    public NettyHttpServer(Integer port) {
        this.port = port;
    }

    public NettyHttpServer(Integer port, DispatcherServlet servlet) {
        this.port = port;
        this.servlet = servlet;
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new HttpServerInitializer(servlet)).option(ChannelOption.SO_BACKLOG, 100)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            log.info("NettyHttpServer Run successfully");
            // 绑定端口，开始接收进来的连接
            ChannelFuture f = b.bind(port).sync();
            // 等待服务器 socket 关闭 。在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("NettySever start fail", e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
