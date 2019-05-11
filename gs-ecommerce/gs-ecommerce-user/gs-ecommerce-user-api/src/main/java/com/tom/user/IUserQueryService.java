package com.tom.user;

import com.tom.user.dto.UserQueryRequest;
import com.tom.user.dto.UserQueryResponse;

/**
 * 腾讯课堂搜索 咕泡学院
 * 加群获取视频：608583947
 * 风骚的Michael 老师
 */
public interface IUserQueryService {


    /**
     * 根据用户id来查询用户信息
     *
     * @param request
     * @return
     */
    UserQueryResponse getUserById(UserQueryRequest request);

    /**
     * 根据用户id来查询用户信息
     *
     * @param request
     * @return
     */
    UserQueryResponse getUserByIdWithLimiter(UserQueryRequest request);
}
