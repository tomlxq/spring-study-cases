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
public class UserRegisterRequest extends AbstractRequest implements Serializable {

    private static final long serialVersionUID = -4807481139973253990L;

    private String username;

    private String password;

    private String mobile;


}
