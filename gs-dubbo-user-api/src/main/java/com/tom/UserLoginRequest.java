package com.tom;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Setter
@Getter
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = -5257196395149859943L;
    String name;
    String password;
}
