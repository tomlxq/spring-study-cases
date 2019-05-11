package com.tom.user.abs;

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
public abstract class AbstractResponse implements Serializable {
    private static final long serialVersionUID = 8122890624127584187L;
    /**
     * 返回码（请求）
     */
    private String code;

    private String msg;


}
