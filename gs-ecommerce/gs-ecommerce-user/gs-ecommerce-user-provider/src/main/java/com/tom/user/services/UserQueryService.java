package com.tom.user.services;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.google.common.util.concurrent.RateLimiter;
import com.tom.user.IUserQueryService;
import com.tom.user.constants.ResponseCodeEnum;
import com.tom.user.dal.entity.User;
import com.tom.user.dal.persistence.UserMapper;
import com.tom.user.dto.UserQueryRequest;
import com.tom.user.dto.UserQueryResponse;
import com.tom.user.exception.ExceptionUtil;
import com.tom.user.exception.ServiceException;
import com.tom.user.exception.ValidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 腾讯课堂搜索 咕泡学院
 * 加群获取视频：608583947
 * 风骚的Michael 老师
 */
@Service("userQueryService")
public class UserQueryService implements IUserQueryService {
    Logger logger = LoggerFactory.getLogger(UserCoreService.class);

    @Autowired
    UserMapper userMapper;

    RateLimiter rateLimiter = RateLimiter.create(2.0);

    public UserQueryResponse getUserById(UserQueryRequest request) {
        UserQueryResponse response = new UserQueryResponse();
        try {
            beforeValidate(request);
            response.setMsg(ResponseCodeEnum.SYS_SUCCESS.getMsg());
            response.setCode(ResponseCodeEnum.SYS_SUCCESS.getCode());
            User user = userMapper.getUserByUid(request.getUid());
            if (user != null) {
                response.setAvatar(user.getAvatar());
                response.setSex(user.getSex());
                response.setRealName(user.getRealname());
                response.setMobile(user.getMobile());
                return response;
            }
            response.setCode(ResponseCodeEnum.QUERY_DATA_NOT_EXIST.getCode());
            response.setMsg(ResponseCodeEnum.QUERY_DATA_NOT_EXIST.getMsg());
        } catch (Exception e) {
            ServiceException serviceException = (ServiceException) ExceptionUtil.handlerException4biz(e);
            response.setCode(serviceException.getErrorCode());
            response.setMsg(serviceException.getErrorMessage());
        }
        return response;
    }

    private void beforeValidate(UserQueryRequest request) {
        if (null == request) {
            throw new ValidateException("请求对象为空");
        }
        if (request.getUid() == null || request.getUid().intValue() == 0) {
            throw new ValidateException("用户id不能为空");
        }
    }

    public UserQueryResponse getUserByIdWithLimiter(UserQueryRequest request) {
        UserQueryResponse response = new UserQueryResponse();
        if (!rateLimiter.tryAcquire()) {
            response.setCode(ResponseCodeEnum.ACCESS_LIMITER.getCode());
            response.setMsg(ResponseCodeEnum.ACCESS_LIMITER.getMsg());
            return response;
        }
        try {
            beforeValidate(request);
            response.setMsg(ResponseCodeEnum.SYS_SUCCESS.getMsg());
            response.setCode(ResponseCodeEnum.SYS_SUCCESS.getCode());
            User user = userMapper.getUserByUid(request.getUid());
            if (user != null) {
                response.setAvatar(user.getAvatar());
                response.setSex(user.getSex());
                response.setRealName(user.getRealname());
                response.setMobile(user.getMobile());
                return response;
            }
            response.setCode(ResponseCodeEnum.QUERY_DATA_NOT_EXIST.getCode());
            response.setMsg(ResponseCodeEnum.QUERY_DATA_NOT_EXIST.getMsg());
        } catch (Exception e) {
            ServiceException serviceException = (ServiceException) ExceptionUtil.handlerException4biz(e);
            response.setCode(serviceException.getErrorCode());
            response.setMsg(serviceException.getErrorMessage());
        }
        return response;
    }
}
