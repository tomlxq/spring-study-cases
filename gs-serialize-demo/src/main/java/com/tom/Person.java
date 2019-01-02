package com.tom;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Person implements Serializable {
    /**
     * java.io.InvalidClassException: com.tom.Person; local class incompatible: stream classdesc serialVersionUID = -11111, local class serialVersionUID = 1142167252366297337
     */
    private static final long serialVersionUID = -11111l;
    /**
     * 序列化并不保持静态变量的状态
     */
    public static int height = 180;
    @Protobuf(fieldType = FieldType.STRING)
    String name;
  /*  @Protobuf(fieldType = FieldType.INT64)
    Timestamp birthday;*/
    /**
     * transient不参与序列化
     */
    @Protobuf(fieldType = FieldType.INT32)
    int age;


}
