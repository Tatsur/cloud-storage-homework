package netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class EchoHandler extends SimpleChannelInboundHandler<String> {


    private EchoServer server;
    private String username;

    public EchoHandler(EchoServer server, String username) {
        this.server = server;
        this.username = username;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Enumeration<ChannelHandlerContext> clients = server.getClients().keys();
        while (clients.hasMoreElements()){
            clients.nextElement().writeAndFlush(username+": "+ s);
        }
        System.out.println("received: "+s);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(username + " , hello into RandomChat!\n");
        server.getClients().put(ctx, username);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client disconnected");
        server.getClients().remove(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
