package com.tom.ecommerce.user.dto;

import com.tom.ecommerce.user.abs.AbstractResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class UserRegisterResponse extends AbstractResponse implements Serializable {
    private static final long serialVersionUID = 2883644932129932586L;
    private Integer uid;
}
