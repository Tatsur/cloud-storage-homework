package netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class FirstIn extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        StringBuilder s = new StringBuilder();
        while (buf.isReadable()){
            s.append((char)buf.readByte());
        }
        System.out.println("received: "+ s);
        ctx.fireChannelRead(s.toString().trim());
    }
}
