package com.foo.concurrent;

/**
 *
 * @ClassName: TestRandom
 * @Description: 【推荐】避免Random实例被多线程使用，虽然共享该实例是线程安全的，但会因竞争同一seed 导致的性能下降。
 * 说明：Random实例包括java.util.Random 的实例或者 Math.random()的方式。
 * 正例：在JDK7之后，可以直接使用API ThreadLocalRandom，而在 JDK7之前，需要编码保证每个线程持有一个实例。
 * @Author: tomluo
 * @Date: 2022/12/17 19:45
 **/
public class TestRandom {}