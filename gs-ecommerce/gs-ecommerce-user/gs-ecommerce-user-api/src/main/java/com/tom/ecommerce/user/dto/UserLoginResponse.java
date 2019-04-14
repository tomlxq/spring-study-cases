package com.tom.ecommerce.user.dto;

import com.tom.ecommerce.user.abs.AbstractResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class UserLoginResponse extends AbstractResponse implements Serializable {
}
