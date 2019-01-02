package com.tom;

import com.tom.clone.Student;
import com.tom.clone.Teacher;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    /**
     * 多次序列化，第二次比第一下多５个字节，是同一个对象的引用？
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Test
    public void testManySerialize() throws IOException, ClassNotFoundException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File("person")));
        Person vo = new Person("tom", 18);
        System.out.println(vo.toString());
        objectOutputStream.writeObject(vo);
        objectOutputStream.flush();
        System.out.println(new File("person").length());
        ObjectInputStream objectInputStream1 = new ObjectInputStream(new FileInputStream(new File("person")));
        Person person1 = (Person) objectInputStream1.readObject();
        objectOutputStream.writeObject(vo);
        objectOutputStream.flush();
        System.out.println(new File("person").length());
        ObjectInputStream objectInputStream2 = new ObjectInputStream(new FileInputStream(new File("person")));
        Person person2 = (Person) objectInputStream2.readObject();
        objectOutputStream.close();
        System.out.println(person1 == person2);
        System.out.println(person1.hashCode());
        System.out.println(person2.hashCode());
    }

    /**
     * 深度克隆 对象不同
     */

    @Test
    public void testDeepClone() throws IOException, ClassNotFoundException {
        Teacher tom = new Teacher("tom", 18);
        Student student2 = new Student("jack", 19, tom);
        System.out.println(student2.hashCode() + " " + student2.toString());
        Student student1 = (Student) student2.deepClone();
        student1.getTeacher().setName("Zhangshan");
        System.out.println(student1.hashCode() + " " + student1.toString());

    }


}
