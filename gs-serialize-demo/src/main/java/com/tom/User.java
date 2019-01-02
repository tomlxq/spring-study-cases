package com.tom;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 如果父类实现了序列化，子类没有序列化　　那么返序列化，子类的属性是没有性的，除非重写父类的方法
 */
@Getter
@Setter
public class User extends SuperUser implements Serializable {
    @Override
    public String toString() {
        return "User{} " + super.toString();
    }
}
