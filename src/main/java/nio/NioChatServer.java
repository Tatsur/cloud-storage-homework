package nio;

import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.io.IOException;
import java.util.*;


public class NioChatServer implements Runnable {
    private final int port;
    private final ServerSocketChannel ssc;
    private final Selector selector;
    private final ByteBuffer buf = ByteBuffer.allocate(256);

    NioChatServer(int port) throws IOException {
        this.port = port;
        this.ssc = ServerSocketChannel.open();
        this.ssc.socket().bind(new InetSocketAddress(port));
        this.ssc.configureBlocking(false);
        this.selector = Selector.open();

        this.ssc.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void run() {
        try {
            System.out.println("Server starting on port " + this.port);

            Iterator<SelectionKey> iter;
            SelectionKey key;
            while (this.ssc.isOpen()) {
                selector.select();
                iter = this.selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    key = iter.next();
                    iter.remove();

                    if (key.isAcceptable()) this.handleAccept(key);
                    if (key.isReadable()) this.handleRead(key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final String WELCOME = "! Welcome to chat!\n";
    private static final String HI = "Hi, ";

    private void handleAccept(SelectionKey key) throws IOException {
        SocketChannel sc = ((ServerSocketChannel) key.channel()).accept();
        User user = new User();
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ, user);
        ByteBuffer welcomeBuffer = ByteBuffer.wrap((HI + user.getUsername() + WELCOME).getBytes());
        sc.write(welcomeBuffer);
        buf.rewind();
        String msg = user.getUsername()+" connected\n";
        broadcast(msg);
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel ch = (SocketChannel) key.channel();
        StringBuilder sb = new StringBuilder();
        buf.clear();
        int read = 0;
        while ((read = ch.read(buf)) > 0) {
            buf.flip();
            byte[] bytes = new byte[buf.limit()];
            buf.get(bytes);
            sb.append(new String(bytes));
            buf.clear();
        }
        String msg;
        User user = (User) key.attachment();
        String username = user.getUsername();
        if (read < 0) {
            msg = username + " left the chat.\n";
            ch.close();
        } else {
            msg = username + ": " + sb.toString();
        }
        String[] data = msg.split(" ", 4);
        String fromUser = data[0];
        String prefix = data[1];
        if (prefix.equals("/pm")) {
            String toUsername = data[2];
            String messageData = data[3];
            String finalMessage = "pm "+ fromUser + messageData;
            for (SelectionKey toKey : selector.keys()) {
                if (toKey.isValid() && toKey.channel() instanceof SocketChannel) {
                    User toUser = (User) toKey.attachment();
                    if (toUsername.equals(toUser.getUsername())) {
                        sendPrivate(finalMessage, toKey);
                        ch.write(ByteBuffer.wrap(("message sent to "+toUser.getUsername()).getBytes()));
                    }
                }
            }
        } else broadcast(msg);
    }

    private void sendPrivate(String msg, SelectionKey key) throws IOException {
        ByteBuffer msgBuffer = ByteBuffer.wrap(msg.getBytes());
        SocketChannel socketChannel = (SocketChannel) key.channel();
        socketChannel.write(msgBuffer);
        msgBuffer.rewind();
    }

    private void broadcast(String msg) throws IOException {
        ByteBuffer msgBuffer = ByteBuffer.wrap(msg.getBytes());
        for (SelectionKey key : selector.keys()) {
            if (key.isValid() && key.channel() instanceof SocketChannel) {
                SocketChannel socketChannel = (SocketChannel) key.channel();
                socketChannel.write(msgBuffer);
                msgBuffer.rewind();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        NioChatServer server = new NioChatServer(8189);
        (new Thread(server)).start();
    }
}
