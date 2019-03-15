package com.tom.protocal;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.msgpack.annotation.Message;

@Getter
@Setter
@Message
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IMMessage {
    /**
     * 客户端IP
     */
    private String ipAddr;
    /**
     * 系统指令
     */
    private String cmd;
    /**
     * 发送时间
     */
    private long time;
    /**
     * 在线人数
     */
    private int online;
    /**
     * 发送者
     */
    private String sender;
    /**
     * 聊天消息
     */
    private String content;
    /**
     * 接收者
     */
    private String receiver;


    public IMMessage(String cmd, long time, String sender) {
        this.cmd = cmd;
        this.time = time;
        this.sender = sender;

    }

    public IMMessage(String cmd, long time, String sender, String content) {
        this.cmd = cmd;
        this.time = time;
        this.sender = sender;
        this.content = content;

    }

    public IMMessage(String cmd, long time, int online, String content) {
        this.cmd = cmd;
        this.time = time;
        this.online = online;
        this.content = content;
    }
}
