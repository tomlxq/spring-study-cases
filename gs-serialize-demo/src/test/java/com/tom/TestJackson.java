package com.tom;

import com.alibaba.fastjson.JSON;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TestJackson {

    public void initial() {

    }

    @Test
    public void testCompare() throws IOException {
        testJackson();
        testFastJson();
        testProtobuf();
        testHessian();
    }

    private void testFastJson() {
        Person person = new Person("tom", 18);

        long start = System.currentTimeMillis();
        String s = null;
        for (int i = 0; i < 10000; i++) {
            s = JSON.toJSONString(person);

        }
        System.out.println("FastJson 总大小：" + s.length() + " 总时间：" + (System.currentTimeMillis() - start) + " ms");
        Person person2 = JSON.parseObject(s, Person.class);
        System.out.println(person2.toString());
    }

    private void testJackson() throws IOException {
        Person person = new Person("tom", 18);
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes = null;
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            bytes = objectMapper.writeValueAsBytes(person);
        }
        System.out.println("jackson 总大小：" + bytes.length + " 总时间：" + (System.currentTimeMillis() - start) + " ms");
        Person person2 = objectMapper.readValue(bytes, Person.class);
        System.out.println(person2.toString());
    }

    private void testProtobuf() throws IOException {
        Person person = new Person("tom", 18);
        Codec<Person> personCodec = ProtobufProxy.create(Person.class, false);
        byte[] bytes = null;
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            bytes = personCodec.encode(person);
        }
        System.out.println("Protobuf 总大小：" + bytes.length + " 总时间：" + (System.currentTimeMillis() - start) + " ms");
        Person person2 = personCodec.decode(bytes);
        System.out.println(person2.toString());
    }

    private void testHessian() throws IOException {
        Person person = new Person("tom", 18);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        HessianOutput hessianOutput = new HessianOutput(byteArrayOutputStream);
        byte[] bytes = null;
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            hessianOutput.writeObject(person);
        }
        System.out.println("Hessian 总大小：" + byteArrayOutputStream.toByteArray().length + " 总时间：" + (System.currentTimeMillis() - start) + " ms");
        HessianInput hessianInput = new HessianInput(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        Person person2 = (Person) hessianInput.readObject();
        System.out.println(person2.toString());
    }
}
