package com.tom.buffer;

import java.nio.IntBuffer;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2020/1/9
 */
@Slf4j
public class IntBufferDemo {
    public static void main(String[] args) {
        // 分配置长度为10的数组
        IntBuffer allocate = IntBuffer.allocate(10);
        printInfo(allocate);
        // IntStream.rangeClosed(1, 4).forEach(idx -> allocate.put(idx));
        // allocate.flip();
        // printInfo(allocate);
        // 分别将数组赋值，并且数组位置已移到最后一个数
        IntStream.rangeClosed(1, allocate.capacity()).forEach(idx -> allocate.put(idx));
        // 重置数组到首位
        allocate.flip();
        printInfo(allocate);
        // 查看当前位置和限制还有没有元素，如果当前位和限制相等，则是最后一个元素
        while (allocate.hasRemaining()) {
            // 取出当前位置的元素值
            System.out.println(allocate.get());
        }
        printInfo(allocate);
    }

    private static void printInfo(IntBuffer allocate) {
        log.info("capacity:" + allocate.capacity());
        log.info("limit:" + allocate.limit());
        log.info("position:" + allocate.position());
    }
}
