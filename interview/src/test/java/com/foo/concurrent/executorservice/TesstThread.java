package com.foo.concurrent.executorservice;

/**
 *
 * @ClassName: TesstThread
 * @Description:
 * 【强制】多线程并行处理定时任务时，Timer运行多个TimeTask时，只要其中之一没有捕获抛出的异常，其它任务便会自动终止运行，
 * 使用ScheduledExecutorService则没有这个问题。
 * @Author: tomluo
 * @Date: 2022/12/17 19:42
 **/
public class TesstThread {}