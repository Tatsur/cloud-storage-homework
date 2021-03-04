package netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.StandardCharsets;

public class Out extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        int len = (int) msg;
        System.out.println("received: "+len);
        ByteBuf buf = ctx.alloc().directBuffer();
        buf.writeBytes((len+"\n").getBytes(StandardCharsets.UTF_8));
        buf.retain();
        ctx.writeAndFlush(buf);
    }
}
