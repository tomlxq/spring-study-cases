package com.tom.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class DoRechargeRequest implements Serializable {
    private static final long serialVersionUID = -8917640994866850536L;
    String name;
}
