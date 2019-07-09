package ru.otus.emlGateway;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EmlGateway {
    private final int BUFFER_SIZE = 1024;
    private final List<Integer> ports;
    private final String host;
    private Selector selector;

    private final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();

    public EmlGateway(String host, Integer... ports) {
        this.host = host;
        this.ports = Arrays.asList(ports);
    }

    private void accept(SelectionKey key) throws IOException {
        System.out.println("accept!");
        SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();

        String remoteAddr = channel.getRemoteAddress().toString();
        System.out.println("Connection Accepted: " + remoteAddr);
        channel.configureBlocking(false);
//        channel.socket().setKeepAlive(true);
        channel.register(selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws IOException {
        System.out.println("read!");
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        while (channel.read(buffer) > 0)
            System.out.println(new String(buffer.array()));
    }

    public void start() {
        try {
            selector = Selector.open();
            for (int port : ports) {
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
                serverSocketChannel.bind(new InetSocketAddress(host, port));
                serverSocketChannel.configureBlocking(false);
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                selector.select();
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    try {
                        if (key.isAcceptable())
                            accept(key);
                        else if (key.isReadable())
                            read(key);

                        else if (key.isConnectable())
                            System.out.println("connect");
                        else if (key.isWritable())
                            System.out.println("write");

                    } catch (IOException e) {
                        e.printStackTrace();
                        key.cancel();
                    } finally {
                        keyIterator.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
