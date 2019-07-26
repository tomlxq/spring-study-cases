package com.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2019/7/19
 */
public class TestJVM {
    public static void main(String[] args) throws IOException {

        while (true) {
            File f = new File("/tmp/test/" + System.currentTimeMillis() + ".txt");
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdir();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(f);
            for (int i = 0; i < 10000; i++) {
                fileOutputStream.write(new byte[256]);
            }
            fileOutputStream.flush();
        }
    }
}
