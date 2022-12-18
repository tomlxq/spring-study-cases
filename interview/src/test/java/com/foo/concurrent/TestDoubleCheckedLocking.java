package com.foo.concurrent;

/**
 *
 * @ClassName: TestDoubleCheckedLocking
 * @Description: 【推荐】在并发场景下，通过双重检查锁（double-checked locking）实现延迟初始化的优化问题隐患(可参考 The "Double-Checked Locking is Broken" Declaration)，推荐解决方案中较为简单一种（适用于JDK5及以上版本），将目标属性声明为 volatile型。
 *
 * @Author: tomluo
 * @Date: 2022/12/17 19:46
 **/

// 反例：
// class Singleton {
// private Helper helper = null;
// public Helper getHelper() {
// if (helper == null)
// synchronized(this) {
// if (helper == null)
// helper = new Helper();
// }
// return helper;
// }
// // other methods and fields...
// }
public class TestDoubleCheckedLocking {}