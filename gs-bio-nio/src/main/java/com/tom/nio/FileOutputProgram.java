package com.tom.nio;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2020/1/9
 */

import static com.tom.Utils.printFileContent;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class FileOutputProgram {
    static private final byte message[] =
        ("You gotta stay focused on your dream.\n" + "你要坚持自己的梦想。").getBytes(StandardCharsets.UTF_8);

    static public void main(String args[]) throws Exception {
        FileOutputStream fout = new FileOutputStream("/tmp/test.txt");

        FileChannel fc = fout.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        for (int i = 0; i < message.length; ++i) {
            buffer.put(message[i]);
        }

        buffer.flip();

        fc.write(buffer);

        fout.close();
        printFileContent("/tmp/test.txt");
    }
}
