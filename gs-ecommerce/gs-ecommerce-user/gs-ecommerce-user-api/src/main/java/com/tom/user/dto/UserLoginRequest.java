package com.tom.user.dto;

import com.tom.user.abs.AbstractRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 腾讯课堂搜索 咕泡学院
 * 加群获取视频：608583947
 * 风骚的Michael 老师
 */
@Getter
@Setter
@ToString
public class UserLoginRequest extends AbstractRequest implements Serializable {
    private static final long serialVersionUID = -5836710058540708731L;

    private String username;

    private String password;


}
