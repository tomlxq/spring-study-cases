package com.tom.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class NioServer {
    public static final Charset CHARSET = Charset.forName("UTF-8");
    public static final String SPLIT_SEP = "#@#";
    private static final List<String> USERS = new ArrayList<String>();
    public static final String USER_EXISTS_MSG = "系统昵称已存在，请更换新的名字！";
    Selector selector = null;

    public NioServer(int port) throws IOException {
        //打开通信
        ServerSocketChannel server = ServerSocketChannel.open();
        //设置关卡
        server.bind(new InetSocketAddress(port));
        server.configureBlocking(false);
        //叫号大厅开始打开迎客了
        selector = Selector.open();
        //可以接待服务了
        server.accept();
        server.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务已启动，监听端口为" + port);
    }

    private void listener() throws IOException {
        while (true) {
            final int select = selector.select();
            if (select == 0) {
                continue;
            }
            Set<SelectionKey> keys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                iterator.remove();
                process(next);
            }
        }


    }

    private void process(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
            key.interestOps(SelectionKey.OP_ACCEPT);
            client.write(CHARSET.encode("请输入你的昵称："));
        }
        if (key.isReadable()) {
            SocketChannel client = (SocketChannel) key.channel();


            StringBuffer stringBuffer = new StringBuffer();
            ByteBuffer allocate = ByteBuffer.allocate(1024);
            try {
                while (client.read(allocate) > 0) {
                    allocate.flip();
                    stringBuffer.append(CHARSET.decode(allocate));

                }
                key.interestOps(SelectionKey.OP_READ);
            } catch (IOException e) {
                key.cancel();
                if (key.channel() != null) {
                    key.channel().close();
                }
            }
            if (stringBuffer.length() > 0) {
                final String[] split = stringBuffer.toString().split(SPLIT_SEP);
                if (split.length == 1) {
                    String nickname = split[0];
                    if (USERS.contains(nickname)) {
                        client.write(CHARSET.encode(USER_EXISTS_MSG));
                    } else {
                        USERS.add(nickname);
                        int onlineCount = getOnlineCount();
                        final String msg = String.format("欢迎【%s】进入聊天室，当前人线为【%s】", nickname, onlineCount);
                        broadcast(null, msg);
                    }
                } else if (split.length > 1) {
                    String nickname = split[0];
                    String message = split[1];
                    if (USERS.contains(nickname)) {
                        final String msg = String.format("【%s】说 %s", nickname, message);
                        broadcast(client, msg);
                    }
                }
            }
        }
    }

    private void broadcast(SocketChannel client, String msg) throws IOException {
        Set<SelectionKey> keys = selector.selectedKeys();
        for (SelectionKey key : keys) {
            SelectableChannel channel = key.channel();
            if (channel instanceof SocketChannel && channel != client) {
                ((SocketChannel) channel).write(CHARSET.encode(msg));
            }
        }
    }

    private int getOnlineCount() {

        int total = 0;
        Set<SelectionKey> keys = selector.keys();
        for (SelectionKey key : keys) {
            Channel channel = key.channel();
            if (channel instanceof SocketChannel) {
                total++;
            }
        }
        return total;
    }

    public static void main(String[] args) throws IOException {
        new NioServer(8080).listener();
    }


}
