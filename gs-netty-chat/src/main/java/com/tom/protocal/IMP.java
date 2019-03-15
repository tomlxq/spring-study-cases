package com.tom.protocal;

public enum IMP {
    /**
     * 系统消息
     */
    SYSTEM("SYSTEM"),
    /**
     * 登入
     */
    LOGIN("LOGIN"),
    /**
     * 登出
     */
    LOGOUT("LOGOUT"),
    /**
     * 送鲜花
     */
    FLOWER("FLOWER"), /**
     * 聊天
     */
    CHAT("CHAT");
    String name;

    private IMP(String name) {
        this.name = name;
    }

    public static boolean isIMP(String name) {
        return name.matches("^\\[(SYSTEM|LOGIN|LOGOUT|FLOURS|CHAT)\\]");
    }

    public String getName() {
        return this.name;
    }
}
