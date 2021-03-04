package netty;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class TestNettyConnection {
    @Before
    public void initServer() throws InterruptedException{
        new Thread(EchoServer::new).start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test() throws IOException {
        Socket socket = new Socket("localhost",8189);
        socket.getOutputStream().write(new byte[]{65});
        socket.getOutputStream().flush();
        Assert.assertEquals(65,socket.getInputStream().read());
    }
}
