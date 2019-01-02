package com.tom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Hello world!
 */
public class AppSubClass {
    public static void main(String[] args) {
        serializedPerson();
        deserializePerson();
    }

    private static void deserializePerson() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File("person")));
            User person = (User) objectInputStream.readObject();
            System.out.println(person.toString());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static void serializedPerson() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File("person")));
            User vo = new User();
            vo.setAge(20);
            System.out.println(vo.toString());
            objectOutputStream.writeObject(vo);
            objectOutputStream.close();
            System.out.println("序列化完成!");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
