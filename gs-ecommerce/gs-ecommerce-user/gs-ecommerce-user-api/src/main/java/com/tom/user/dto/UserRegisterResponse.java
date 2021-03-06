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
public class UserRegisterResponse extends AbstractResponse implements Serializable {

    private static final long serialVersionUID = -7690077437344492561L;

    private Integer uid;


}
