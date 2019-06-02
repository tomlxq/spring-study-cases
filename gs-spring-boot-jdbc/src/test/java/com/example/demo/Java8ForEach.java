package com.example.demo;


import com.example.demo.domain.Person;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Java8ForEach {
    // Program to Iterate Map using keySet() in Java

    public static void filter(List<String> names, Predicate condition) {
        for (String name : names) {
            if (condition.test(name)) {
                System.out.println(name + " ");
            }
        }
    }

    @Test
    public void test4() {
        new Thread(() -> System.out.println("In Java8!")).start();
    }

    @Test
    public void test15() {
        List<String> languages = Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp");

        System.out.println("Languages which starts with J :");
        filter(languages, (str) -> ((String) str).startsWith("J"));

        System.out.println("Languages which ends with a ");
        filter(languages, (str) -> ((String) str).endsWith("a"));

        System.out.println("Print all languages :");
        filter(languages, (str) -> true);

        System.out.println("Print no language : ");
        filter(languages, (str) -> false);

        System.out.println("Print language whose length greater than 4:");
        filter(languages, (str) -> ((String) str).length() > 4);

    }

    @Test
    public void test14() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        // 可改变对象
        list.stream().map((i) -> i * 3).forEach(System.out::println);

        // 不可改变元有对象
        list.forEach(i -> i = i * 3);
        list.forEach(System.out::println);
    }

    @Test
    public void test16() {
        Predicate<String> startWithJ = (n) -> n.startsWith("J");
        Predicate<String> fourLength = (n) -> n.length() == 4;

        List<String> languages = Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp");
        languages.stream().filter(startWithJ.and(fourLength))
                .forEach(System.out::println);
    }

    @Test
    public void test17() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        Integer integer = list.stream().map((i) -> i = i * 3)
                .reduce((sum, count) -> sum += count).get();

        System.out.println(integer);
    }

    @Test
    public void test11() {
        // 字符串连接，concat = "ABCD"
        String concat = Stream.of("A", "B", "C", "D").reduce("", String::concat);
        // 求最小值，minValue = -3.0
        double minValue = Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double.MAX_VALUE, Double::min);
        // 求和，sumValue = 10, 有起始值
        int sumValue = Stream.of(1, 2, 3, 4).reduce(0, Integer::sum);
        // 求和，sumValue = 10, 无起始值
        sumValue = Stream.of(1, 2, 3, 4).reduce(Integer::sum).get();
        // 过滤，字符串连接，concat = "ace"
        concat = Stream.of("a", "B", "c", "D", "e", "F").
                filter(x -> x.compareTo("Z") > 0).
                reduce("", String::concat);
    }

    @Test
    public void test10() {
        //获取数字的个数、最小值、最大值、总和以及平均值
        List<Integer> primes = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
        IntSummaryStatistics stats = primes.stream().mapToInt((x) -> x).summaryStatistics();
        System.out.println("Highest prime number in List : " + stats.getMax());
        System.out.println("Lowest prime number in List : " + stats.getMin());
        System.out.println("Sum of all prime numbers : " + stats.getSum());
        System.out.println("Average of all prime numbers : " + stats.getAverage());
    }

    @Test
    public void test1() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        // 直接打印
        list.forEach(System.out::println);

        // 取值分别操作
        list.forEach(i -> {
            System.out.println(i * 3);
        });
        List<String> strList = Arrays.asList("abc", "eqwr", "bcd", "qb", "ehdc", "jk");
        List<String> filtered = strList.stream().filter(x -> x.length() > 2).collect(Collectors.toList());
        System.out.printf("Original List : %s, filtered list : %s %n", strList, filtered);
    }

    @Test
    public void test3() {
        List<String> strList = Arrays.asList("abc", "eqwr", "bcd", "qb", "ehdc", "jk");
        String collect = strList.stream().map(x -> x.toUpperCase()).collect(Collectors.joining(", "));
        System.out.printf("filtered list : %s %n", collect);
    }

    @Test
    public void test7() {
        List<Integer> numbers = Arrays.asList(9, 10, 3, 4, 7, 3, 4);
        List<Integer> distinct = numbers.stream().map(i -> i * i).distinct().collect(Collectors.toList());
        System.out.printf("Original List : %s,  Square Without duplicates : %s %n", numbers, distinct);
    }

    @Test
    public void test8() {
        String str = "abc";
        Optional.ofNullable(str).ifPresent(System.out::println);
    }

    @Test
    public void test9() {
        String str = "abc";
        // Java 8
        Optional.ofNullable(str).map(String::length).orElse(-1);
        // Pre-Java 8
        // return if (text != null) ? text.length() : -1;
    }

    @Test
    public void test2() {
        //使用String默认的排序规则，比较的是Person的name字段
        Comparator<Person> byName = Comparator.comparing(p -> p.getName());
        //不用写传入参数,传入的用Person来声明
        Comparator<Person> byName2 = Comparator.comparing(Person::getName);
    }

    @Test
    public void test() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "One");
        map.put(2, "Two");

        // 1. using Iterator
        Iterator<Integer> itr = map.keySet().iterator();
        while (itr.hasNext()) {
            Integer key = itr.next();
            String value = map.get(key);
            System.out.println(key + "=" + value);
        }

        // 2. For-each Loop
        for (Integer key : map.keySet()) {
            System.out.println(key + "=" + map.get(key));
        }

        // 3. Java 8 - Iterator.forEachRemaining()
        map.keySet()
                .iterator()
                .forEachRemaining(key -> System.out.println(key + "=" + map.get(key)));

        // 4. Java 8 - Stream.forEach()
        map.keySet().stream()
                .forEach(key -> System.out.println(key + "=" + map.get(key)));

        // 5. Java 8 - Stream.of() + toArray() + forEach()
        Stream.of(map.keySet().toArray())
                .forEach(key -> System.out.println(key + "=" + map.get(key)));

        map.forEach((k, v) -> {
            System.out.println(v);
        });
        List<String> list = new ArrayList<String>(Arrays.asList("zhangshan", "lishi"));
        list.forEach(k -> {
            System.out.println(k);
        });

        IntStream.range(0, list.size())
                .forEach(idx ->
                        System.out.println(idx + list.get(idx)
                        ));
    }
}
