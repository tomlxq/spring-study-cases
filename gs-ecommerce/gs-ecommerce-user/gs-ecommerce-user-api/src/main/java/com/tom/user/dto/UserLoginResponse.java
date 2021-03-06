package com.tom.user.dto;

import com.tom.user.abs.AbstractResponse;
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
public class UserLoginResponse extends AbstractResponse implements Serializable {

    private static final long serialVersionUID = 2073944735436288078L;
    private String realName;

    private String avatar;

    private String mobile;

    private String sex;


}
