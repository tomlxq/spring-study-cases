package com.tom.ecommerce.user.dto;

import com.tom.ecommerce.user.abs.AbstractRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class UserLoginRequest extends AbstractRequest implements Serializable {
}
