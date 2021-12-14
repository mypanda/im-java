package io.github.mypanda.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

/*
    处理消息的handler
    TextWebSocketFrame 在netty中，是用于为websocket专门处理文本对象，frame是消息载体
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 3.用于记录和管理所有客户端的channel
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 1. 获取客户端传递过来的消息
        String content = msg.text();
        System.out.println("接受到数据：" + content);

        // 4. 把数据传递到所有的客户端
        /*for (Channel channel: clients){
            channel.writeAndFlush(new TextWebSocketFrame("[服务器在]"
                    + LocalDateTime.now()
                    + "接收到信息,消息为"
                    + content));
        }*/
        clients.writeAndFlush(new TextWebSocketFrame("[服务器在]"
                + LocalDateTime.now()
                + "接收到信息,消息为"
                + content));
    }

    /*
        当客户端链接服务端之后（打开链接）
        获取客户端的channel，并且放到channelGroup中去进行管理
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 2.
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当触发handlerRemoved，channelGroup会自动移除对应客户端的channel
        // clients.remove(ctx.channel());

        System.out.println("客户端断开，channel对应的长id为："
                + ctx.channel().id().asLongText());
        System.out.println("客户端断开，channel对应的短id为："
                + ctx.channel().id().asShortText());
    }
}
