package com.tom.user.dal.persistence;


import com.tom.user.dal.entity.User;

/**
 * 腾讯课堂搜索 咕泡学院
 * 加群获取视频：608583947
 * 风骚的Michael 老师
 */
public interface UserMapper {

    /**
     * 根据用户名查询用户信息
     *
     * @param username
     * @return
     */
    User getUserByUserName(String username);

    /**
     * 根据uid获取用户信息
     *
     * @param uid
     * @return
     */
    User getUserByUid(Integer uid);

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    int insertSelective(User user);
}
