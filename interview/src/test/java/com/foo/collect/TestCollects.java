package com.foo.collect;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.*;

import org.junit.Test;

import com.foo.entity.Student;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @ClassName: TestCollects
 * @Description:
 * @Author: tomluo
 * @Date: 2022/12/18 14:25
 **/
@Slf4j
public class TestCollects {
    /**
     * 【强制】关于hashCode和equals的处理，遵循如下规则：
     * 1） 只要重写equals，就必须重写hashCode。
     * 2） 因为Set存储的是不重复的对象，依据hashCode和equals进行判断，所以Set存储的对象必须重写这两个方法。
     * 3） 如果自定义对象作为Map的键，那么必须重写hashCode和equals。
     * 说明：String重写了hashCode和equals方法，所以我们可以非常愉快地使用String对象作为key来使用。
     */
    /**
     * 【强制】 ArrayList的subList结果不可强转成ArrayList，否则会抛出ClassCastException异常，即java.util.RandomAccessSubList cannot be cast to java.util.ArrayList.
     * 说明：subList 返回的是 ArrayList 的内部类 SubList，并不是 ArrayList ，而是 ArrayList 的一个视图，对于SubList子列表的所有操作最终会反映到原列表上。
     */
    /**
     * 【强制】在subList场景中，高度注意对原集合元素个数的修改，会导致子列表的遍历、增加、删除均会产生ConcurrentModificationException 异常。
     */
    /**
     * 【强制】使用集合转数组的方法，必须使用集合的toArray(T[] array)，传入的是类型完全一样的数组，大小就是list.size()。
     * 说明：使用toArray带参方法，入参分配的数组空间不够大时，toArray方法内部将重新分配内存空间，并返回新数组地址；如果数组元素个数大于实际所需，下标为[ list.size() ]的数组元素将被置为null，其它数组元素保持原值，因此最好将方法入参数组大小定义与集合元素个数一致。
     */
    @Test
    public void testList() {
        List<String> list = new ArrayList<String>(2);
        list.add("guan");
        list.add("bao");
        String[] array = new String[list.size()];
        array = list.toArray(array);
    }

    /**
     * 【强制】使用工具类Arrays.asList()把数组转换成集合时，不能使用其修改集合相关的方法，它的add/remove/clear方法会抛出UnsupportedOperationException异常。
     * 说明：asList的返回对象是一个Arrays内部类，并没有实现集合的修改方法。Arrays.asList体现的是适配器模式，只是转换接口，后台的数据仍是数组。
     * String[] str = new String[] { "you", "wu" };
     * List list = Arrays.asList(str);
     * 第一种情况：list.add("yangguanbao"); 运行时异常。
     * 第二种情况：str[0] = "gujin"; 那么list.get(0)也会随之修改。
     */

    @Test(expected = UnsupportedOperationException.class)
    public void testArrayList() {
        String[] str = new String[] {"you", "wu"};
        List list = Arrays.asList(str);
        str[0] = "gujin";
        assertThat(list.get(0)).isEqualTo("gujin");
        list.add("yangguanbao");
    }

    /**
     * 【强制】泛型通配符<? extends T>来接收返回的数据，此写法的泛型集合不能使用add方法，而<? super T>不能使用get方法，作为接口调用赋值时易出错。
     * 说明：扩展说一下PECS(Producer Extends Consumer Super)原则：
     * 第一、频繁往外读取内容的，适合用<? extends T>。
     * 第二、经常往里插入的，适合用<? super T>。
     */
    /**
     * 【强制】不要在foreach循环里进行元素的remove/add操作。remove元素请使用Iterator方式，如果并发操作，需要对Iterator对象加锁。
     */
    @Test
    public void testIteratorRemoveAndAdd() {
        List<String> list = newArrayList("111", "333");
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String item = iterator.next();
            if (item.equals("333")) {
                iterator.remove();
            }
        }
        log.info("{}", list);
        assertThat(list).isEqualTo(newArrayList("111"));
    }

    @Test
    public void testRemove() {
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        for (String item : list) {
            if ("1".equals(item)) {
                list.remove(item);
            }
        }
        log.info("{}", list);
    }

    /**
     * 【强制】 在JDK7版本及以上，Comparator要满足如下三个条件，不然Arrays.sort，Collections.sort会报IllegalArgumentException异常。
     * 说明：三个条件如下：
     * 1） x，y的比较结果和y，x的比较结果相反。
     * 2） x>y，y>z，则x>z。
     * 3） x=y，则x，z比较结果和y，z比较结果相同。
     * 反例：下例中没有处理相等的情况，实际使用中可能会出现异常：
     * new Comparator<Student>() {
     *           @Override
     *           public int compare(Student o1, Student o2) {
     *             return o1.getId() > o2.getId() ? 1 : -1;
     *           }
     *     };
     */

    @Test
    public void testComparator() {
        Comparator<Student> comparator = new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return o1.getId() > o2.getId() ? 1 : -1;
            }
        };
        Student student = Student.builder().id(1).name("x").build();
        Student student2 = Student.builder().id(1000).name("x").build();
        Student student3 = Student.builder().id(500).name("x").build();
        Student student4 = Student.builder().id(1000).name("x").build();
        List<Student> list = newArrayList(student, student2, student3, student4);
        Student[] array = new Student[list.size()];
        array = list.toArray(array);
        Arrays.sort(array, comparator);
        log.info("{}", array);
        Collections.sort(list, comparator);
        log.info("{}", array);
    }
}
