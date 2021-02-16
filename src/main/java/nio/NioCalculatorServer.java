package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_READ;

public class NioCalculatorServer {

    private ServerSocketChannel serverChannel;
    private Selector selector;
    private ByteBuffer buffer;

    public NioCalculatorServer() throws IOException {
        buffer = ByteBuffer.allocate(256);
        serverChannel = ServerSocketChannel.open();
        selector = Selector.open();
        serverChannel.bind(new InetSocketAddress(8189));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, OP_ACCEPT);
        while (serverChannel.isOpen()) {
            selector.select(); // block
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = keys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if (key.isAcceptable()) {
                    handleAccept(key);
                }
                if (key.isReadable()) {
                    handleRead(key);
                }
                keyIterator.remove();
            }
        }
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        int read = channel.read(buffer);
        if (read == -1) {
            return;
        }
        buffer.flip();
        StringBuilder msg = new StringBuilder();
        while (buffer.hasRemaining()) {
            msg.append((char) buffer.get());
        }
        buffer.clear();
        System.out.println("received: " + msg);
        String[] args = msg.toString().trim().split(" ");
        System.out.println(Arrays.toString(args));
        int sum = Integer.parseInt(args[0]) + Integer.parseInt(args[1]);
        System.out.println(sum);
        channel.write(ByteBuffer.wrap((sum + "\n").getBytes(StandardCharsets.UTF_8)));
    }

    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = channel.accept();
        socketChannel.write(ByteBuffer.wrap(
                "Hello to calc server!\nInput two args for calculate sum:\n".getBytes(StandardCharsets.UTF_8)));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, OP_READ);
    }

    public static void main(String[] args) throws IOException {
        new NioCalculatorServer();
    }
}
