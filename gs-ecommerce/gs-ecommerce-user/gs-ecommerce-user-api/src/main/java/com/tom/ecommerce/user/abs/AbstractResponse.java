package com.tom.ecommerce.user.abs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public abstract class AbstractResponse implements Serializable {


    private static final long serialVersionUID = -3209033550017849216L;
    /**
     * 返回码（请求）
     */
    private String code;

    private String msg;
}
