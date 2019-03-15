package com.tom.protocal;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;


public class IMProcessor {
    private final static ChannelGroup onlineUsers = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private IMDecoder decoder = new IMDecoder();
    private IMEncoder encoder = new IMEncoder();
    private AttributeKey<String> NICK_NAME = AttributeKey.valueOf("NICK_NAME");
    private AttributeKey<String> IP_ADDR = AttributeKey.valueOf("IP_ADDR");
    private AttributeKey<JSONObject> ATTRS = AttributeKey.valueOf("ATTRS");

    /**
     * 获取用户远程IP地址
     *
     * @param client
     * @return
     */
    public String getAddress(Channel client) {
        return client.remoteAddress().toString().replaceFirst("/", "");
    }

    public void process(Channel client, String msg) {
        IMMessage request = decoder.decode(msg);
        if (null == request) {
            return;
        }
        String addr = getAddress(client);
        final String nickName = request.getSender();
        if (request.getCmd().equals(IMP.LOGIN.getName())) {
            client.attr(NICK_NAME).getAndSet(request.getSender());
            client.attr(IP_ADDR).getAndSet(addr);
            //client.attr(ATTRS).getAndSet(request.get());
            onlineUsers.add(client);
            for (Channel c : onlineUsers) {
                IMMessage temMsg = null;
                if (c != client) {
                    temMsg = new IMMessage(IMP.SYSTEM.getName(), sysTime(), onlineUsers.size(), nickName + "已加入聊天室");
                } else {
                    temMsg = new IMMessage(IMP.SYSTEM.getName(), sysTime(), onlineUsers.size(), "您与服务器已建立联接");
                }
                c.writeAndFlush(new TextWebSocketFrame(encoder.encode(temMsg)));
            }
        } else if (request.getCmd().equals(IMP.CHAT.getName())) {
            for (Channel c : onlineUsers) {
                if (c == client) {
                    request.setSender("You");
                } else {
                    request.setSender(c.attr(NICK_NAME).get());
                }

                c.writeAndFlush(new TextWebSocketFrame(encoder.encode(request)));
            }
        } else if (request.getCmd().equals(IMP.FLOWER.getName())) {
            JSONObject attrs = getAttrs(client);
            long currTime = sysTime();
            if (null != attrs) {
                long lastTime = attrs.getLongValue("lastFlowerTime");
                //60秒之内不允许重复刷鲜花
                int secends = 10;
                long sub = currTime - lastTime;
                if (sub < 1000 * secends) {
                    request.setSender("you");
                    request.setCmd(IMP.SYSTEM.getName());
                    request.setContent("您送鲜花太频繁," + (secends - Math.round(sub / 1000)) + "秒后再试");
                    String content = encoder.encode(request);
                    client.writeAndFlush(new TextWebSocketFrame(content));
                    return;
                }
            }

            //正常送花
            for (Channel channel : onlineUsers) {
                if (channel == client) {
                    request.setSender("you");
                    request.setContent("你给大家送了一波鲜花雨");
                    setAttrs(client, "lastFlowerTime", currTime);
                } else {
                    request.setSender(getNickName(client));
                    request.setContent(getNickName(client) + "送来一波鲜花雨");
                }
                request.setTime(sysTime());

                String content = encoder.encode(request);
                channel.writeAndFlush(new TextWebSocketFrame(content));
            }
        } else if (request.getCmd().equals(IMP.LOGOUT.getName())) {
            onlineUsers.remove(client);
        }


    }


    /**
     * 获取扩展属性
     *
     * @param client
     * @return
     */
    public JSONObject getAttrs(Channel client) {
        try {
            return client.attr(ATTRS).get();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取扩展属性
     *
     * @param client
     * @return
     */
    private void setAttrs(Channel client, String key, Object value) {
        try {
            JSONObject json = client.attr(ATTRS).get();
            json.put(key, value);
            client.attr(ATTRS).set(json);
        } catch (Exception e) {
            JSONObject json = new JSONObject();
            json.put(key, value);
            client.attr(ATTRS).set(json);
        }
    }

    private String getNickName(Channel client) {
        return client.attr(NICK_NAME).get();
    }

    private long sysTime() {
        return System.currentTimeMillis();
    }

    public void logout(Channel client) {
        onlineUsers.remove(client);
    }

    ;
}




