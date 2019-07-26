package com.example;

import java.io.File;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2019/7/26
 */
public class HelloWorldDemo {
    private Object obj = new Object();

    public void methodOne(int i) {
        int j = 0;
        int sum = j + i;
        Object acb = obj;
        long start = System.currentTimeMillis();
        methodTwo();
        return;
    }

    private void methodTwo() {
        File file = new File("");
    }
}
