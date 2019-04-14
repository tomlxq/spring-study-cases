package com.tom.ecommerce.user.dto;

import com.tom.ecommerce.user.abs.AbstractRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class UserRegisterRequest extends AbstractRequest implements Serializable {

    private static final long serialVersionUID = -1959963353045226826L;
    private String username;

    private String password;

    private String mobile;
}
