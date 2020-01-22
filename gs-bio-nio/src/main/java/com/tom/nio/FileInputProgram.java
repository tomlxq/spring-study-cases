package com.tom.nio;

import static com.tom.Utils.writeFileContent;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2020/1/9
 */
public class FileInputProgram {
    static public void main(String args[]) throws Exception {
        writeFileContent("/tmp/test.txt", "Hello,HanMeiMei\r\nHello,Polly");
        FileInputStream fin = new FileInputStream("/tmp/test.txt");

        // 获取通道
        FileChannel fc = fin.getChannel();

        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 读取数据到缓冲区
        fc.read(buffer);

        buffer.flip();

        while (buffer.remaining() > 0) {
            byte b = buffer.get();
            System.out.print(((char)b));
        }

        fin.close();
    }
}