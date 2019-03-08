package com.tom.nio;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import static com.tom.nio.NioServer.CHARSET;
import static com.tom.nio.NioServer.SPLIT_SEP;

public class NioClient {
    private final InetSocketAddress sockAddress = new InetSocketAddress("localhost", 8080);
    Selector selector = null;
    ;
    private String nickname = null;
    SocketChannel client = null;

    public NioClient() throws IOException {
        client = SocketChannel.open(sockAddress);
        client.configureBlocking(false);
        selector = Selector.open();
        client.register(selector, SelectionKey.OP_READ);
    }

    public static void main(String[] args) throws IOException {
        new NioClient().session();
    }

    private void session() {
        new Read().start();
        new Write().start();

    }

    private class Write extends Thread {
        @Override
        public void run() {
            try {
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNextLine()) {

                    String line = scanner.nextLine();
                    if (StringUtils.isEmpty(line)) continue;
                    if (StringUtils.equals("", nickname)) {
                        nickname = line;
                        line = nickname + SPLIT_SEP;
                    } else {
                        line = nickname + SPLIT_SEP + line;
                    }
                    client.write(CHARSET.encode(line));


                }
                scanner.close();
            } catch (IOException e) {

            }

        }


    }

    private class Read extends Thread {


        @Override
        public void run() {
            try {
                while (true) {


                    int select = selector.select();

                    if (select == 0) return;
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey next = iterator.next();
                        iterator.remove();
                        process(next);
                    }


                }
            } catch (IOException e) {
                // e.printStackTrace();
            }

        }

        private void process(SelectionKey key) throws IOException {

            if (!key.isReadable()) return;
            ByteBuffer allocate = ByteBuffer.allocate(1024);
            SocketChannel channel = (SocketChannel) key.channel();
            String content = new String();
            while (channel.read(allocate) > 0) {
                allocate.flip();
                content += CHARSET.decode(allocate);
            }

            if (content.equals(NioServer.USER_EXISTS_MSG)) {
                nickname = "";
            }
            System.out.println(content);
            key.interestOps(SelectionKey.OP_READ);
        }
    }


}
