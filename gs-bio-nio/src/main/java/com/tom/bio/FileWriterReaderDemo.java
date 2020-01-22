package com.tom.bio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2020/1/5
 */
public class FileWriterReaderDemo {
    public static void main(String[] args) throws IOException {
        String filePath = "/tmp/test.txt";
        String content = "最近有什么好看的电影?";
        // Java 7 之前文件的读取是这样的
        // 添加文件
        FileWriter fileWriter = new FileWriter(filePath, true);
        fileWriter.write(content);
        fileWriter.close();
        // 读取文件
        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuffer bf = new StringBuffer();
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            bf.append(str + "\n");
        }
        bufferedReader.close();
        fileReader.close();
        System.out.println(bf.toString());
        // Java 7 引入了Files（java.nio包下）的，大大简化了文件的读写
        // 写入文件（追加方式：StandardOpenOption.APPEND）
        Files.write(Paths.get(filePath), content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        // 读取文件
        byte[] data = Files.readAllBytes(Paths.get(filePath));
        System.out.println(new String(data, StandardCharsets.UTF_8));

        // 创建多（单）层目录（如果不存在创建，存在不会报错）
        new File("//tmp//a//b").mkdirs();
    }
}
