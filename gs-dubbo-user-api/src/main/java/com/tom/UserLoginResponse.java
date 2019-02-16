package com.tom;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter
@Setter
public class UserLoginResponse implements Serializable {
    private static final long serialVersionUID = -7632536404653590126L;
    String code;
    String name;
}
