package io.github.mypanda.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/*
    实现客户端发送一个请求，服务器发返回hello netty
 */
public class HelloServer {
    public static void main(String[] args) throws Exception {
        // 定义一对像线程组
        // 定义主线程组,用于接受客户端的链接，但是不做任何处理
        EventLoopGroup masterGroup = new NioEventLoopGroup();
        // 定义从线程组，master线程组会把任务丢给它，让手下线程组做任务
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建netty服务器，ServerBootstrap是一个启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(masterGroup,workerGroup)      // 设置主从线程组
                    .channel(NioServerSocketChannel.class)        // 设置Nio的双向通道
                    .childHandler(new HelloServerInitializer());// 子处理器，用于处理workerGroup

            // 启动服务，并且绑定8088为启动端口,同时启动方式为同步
            ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();

            // 监听关闭的channel，设置为同步方式
            channelFuture.channel().closeFuture().sync();
        }finally {
            // 关闭group
            masterGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
