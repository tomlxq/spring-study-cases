package com.tom.msg;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class InvokerMsg implements Serializable {
    private static final long serialVersionUID = -7323377780138050176L;
    String clazzName;//调用的是哪个类
    String method;//调用的那个方法
    Class<?>[] params;//传入的参数是什么
    Object[] values;//值是什么

}
