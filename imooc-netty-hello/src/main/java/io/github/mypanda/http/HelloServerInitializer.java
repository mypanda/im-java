package io.github.mypanda.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/*
    初始化器，channel注册后，会执行里面相应的初始化方法
 */
public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        // 通过SocketChannel去获取对应的管道
        ChannelPipeline pipeline = channel.pipeline();

        // 通过管道，添加handler
        // HttpServerCodec是由netty提供的助手类，可以理解为拦截器
        pipeline.addLast("HttpServerCodec",new HttpServerCodec());

        // 添加自定义的助手类，返回hello netty~
        pipeline.addLast("CustomHandler",new CustomHandler());
    }
}
