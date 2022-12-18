package com.foo.concurrent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 *
 * @ClassName: com.foo.concurrent.NoneSimpleDateFormat
 * @Description:【强制】SimpleDateFormat 是线程不安全的类，一般不要定义为static变量，如果定义为static，必须加锁，或者使用DateUtils工具类。
 * 正例：注意线程安全，使用DateUtils
 * 说明：如果是JDK8的应用，可以使用Instant代替Date，LocalDateTime代替Calendar，DateTimeFormatter代替SimpleDateFormat
 * @Author: tomluo
 * @Date: 2022/12/17 16:32
 **/
public class NoneSimpleDateFormat {
    /**
     * 使用Instant代替Date
     */
    Instant date = Instant.now();
    /**
     * LocalDateTime代替Calendar
     */
    LocalDateTime localDateTime = LocalDateTime.ofInstant(date, ZoneId.systemDefault());
    /**
     * DateTimeFormatter代替SimpleDateFormat
     */
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
}