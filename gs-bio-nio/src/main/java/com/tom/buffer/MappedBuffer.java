package com.tom.buffer;

import static com.tom.Utils.printFileContent;
import static com.tom.Utils.writeFileContent;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2020/1/9
 */
public class MappedBuffer {
    static private final int start = 0;
    static private final int size = 1024;

    static public void main(String args[]) throws Exception {
        writeFileContent("/tmp/io.txt", "hello,world");
        RandomAccessFile raf = new RandomAccessFile("/tmp/io.txt", "rw");
        printFileContent("/tmp/io.txt");
        FileChannel fc = raf.getChannel();

        // 把缓冲区跟文件系统进行一个映射关联
        // 只要操作缓冲区里面的内容，文件内容也会跟着改变
        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, start, size);

        mbb.put(0, (byte)97);
        mbb.put(1023, (byte)122);

        raf.close();
        printFileContent("/tmp/io.txt");
    }
}
