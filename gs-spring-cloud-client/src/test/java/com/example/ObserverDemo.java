package com.example;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 功能描述
 *
 * @author hp
 * @email 72719046@qq.com
 * @date 2019/6/16
 */
public class ObserverDemo {
    public static void main(String[] args) {

        MyObservable observable = new MyObservable();
        // 发布者发布通知 订阅者被感知
        observable.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object value) {
                System.out.println(value);
            }
        });
        observable.setChanged();
        observable.notifyObservers("hello,world!");
        testIterator();
    }

    private static void testIterator() {
        List<Integer> list = Arrays.asList(new Integer[] {1, 2, 3, 4, 5});
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {// 通过循环主动获取
            System.out.println(iterator.next());
        }
    }

    public static class MyObservable extends Observable {
        @Override
        protected synchronized void setChanged() {
            super.setChanged();
        }
    }
}
