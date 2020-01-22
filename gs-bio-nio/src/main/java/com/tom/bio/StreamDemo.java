package com.tom.bio;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.stream.Stream;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2020/1/5
 */
public class StreamDemo {
    public static void main(String[] args) throws IOException {
        OutputStream outputStream = new FileOutputStream("/tmp/log.txt", true); // 参数二，表示是否追加，true=追加
        outputStream.write("你好，老王".getBytes("utf-8"));
        outputStream.close();
        InputStream inputStream = new FileInputStream("/tmp/log.txt");
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        String str = new String(bytes, "utf-8");
        System.out.println(str);
        inputStream.close();

        Writer writer = new FileWriter("/tmp/log1.txt", true); // 参数二，是否追加文件，true=追加
        writer.append("老王，你好");
        writer.close();

        Reader reader = new FileReader("/tmp/log1.txt");
        BufferedReader bufferedReader = new BufferedReader(reader);
        final Stream<String> lines = bufferedReader.lines();
        lines.forEach(line -> {
            System.out.println(line);
        });
        bufferedReader.close();
        reader.close();
    }
}
