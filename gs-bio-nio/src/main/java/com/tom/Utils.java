package com.tom;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2020/1/9
 */
public class Utils {
    public static void writeFileContent(String pathname, String content) throws IOException {
        FileUtils.write(new File(pathname), content);
    }

    public static void printFileContent(String pathname) throws IOException {
        System.out.println(new String(FileUtils.readFileToByteArray(new File(pathname)), StandardCharsets.UTF_8));
    }
}
