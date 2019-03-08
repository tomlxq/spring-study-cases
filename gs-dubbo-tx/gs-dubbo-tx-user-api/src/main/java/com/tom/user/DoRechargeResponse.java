package com.tom.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class DoRechargeResponse implements Serializable {
    private static final long serialVersionUID = -9060172606343518233L;
    Object data;
    String code;
    String memo;
}
