package com.tom.bio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2020/1/9
 */
public class BIOReadFile {
    public static void main(String[] args) {
        try {
            FileUtils.writeLines(new File("/tmp/io.txt"),
                Arrays.asList("姓名：张三", "年龄：18", "email：tom@qq.com", "phone：135****7853"));
            FileInputStream input = new FileInputStream("/tmp/io.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String nameLine = reader.readLine();
            String ageLine = reader.readLine();
            String emailLine = reader.readLine();
            String phoneLine = reader.readLine();
            String lastLine = reader.readLine();
            System.out.println(nameLine);
            System.out.println(ageLine);
            System.out.println(emailLine);
            System.out.println(phoneLine);
            System.out.println(lastLine);

            input.close();
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
