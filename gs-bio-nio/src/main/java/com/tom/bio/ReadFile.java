package com.tom.bio;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadFile {
    public static void main(String[] args) {

        InputStream resourceAsStream = ReadFile.class.getClassLoader().getResourceAsStream("bio.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
        try {
            String name = bufferedReader.readLine();
            String age = bufferedReader.readLine();
            String sex = bufferedReader.readLine();
            String readLine = bufferedReader.readLine();
            System.out.println("name:" + name);
            System.out.println("age:" + age);
            System.out.println("sex:" + sex);
            System.out.println("readLastLine:" + readLine);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(bufferedReader);
            IOUtils.closeQuietly(resourceAsStream);
        }
    }

}
