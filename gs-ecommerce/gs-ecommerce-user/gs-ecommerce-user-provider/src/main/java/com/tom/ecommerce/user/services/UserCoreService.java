package com.tom.ecommerce.user.services;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.tom.ecommerce.user.IUserCoreService;
import com.tom.ecommerce.user.constants.Constants;
import com.tom.ecommerce.user.constants.ResponseCodeEnum;
import com.tom.ecommerce.user.dal.entity.User;
import com.tom.ecommerce.user.dal.persistence.UserMapper;
import com.tom.ecommerce.user.dto.UserLoginRequest;
import com.tom.ecommerce.user.dto.UserLoginResponse;
import com.tom.ecommerce.user.dto.UserRegisterRequest;
import com.tom.ecommerce.user.dto.UserRegisterResponse;
import com.tom.ecommerce.user.exception.ExceptionUtil;
import com.tom.ecommerce.user.exception.ServiceException;
import com.tom.ecommerce.user.exception.ValidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("userLoginService")
public class UserCoreService implements IUserCoreService {
    Logger logger = LoggerFactory.getLogger(UserCoreService.class);
    @Autowired
    UserMapper userMapper;

    @Override
    public UserLoginResponse login(UserLoginRequest userLoginRequest) {
        return null;
    }

    @Override
    public UserRegisterResponse register(UserRegisterRequest userRegisterRequest) {
        logger.info("begin UserCoreService.register,request:【" + userRegisterRequest + "】");

        UserRegisterResponse response = new UserRegisterResponse();
        try {
            beforeRegisterValidate(userRegisterRequest);

            User user = new User();
            user.setUsername(userRegisterRequest.getUsername());
            user.setPassword(userRegisterRequest.getPassword());
            user.setStatus(Constants.FORZEN_USER_STATUS);
            user.setCreateTime(new Date());

            int effectRow = userMapper.insertSelective(user);
            if (effectRow > 0) {
                response.setCode(ResponseCodeEnum.SYS_SUCCESS.getCode());
                response.setMsg(ResponseCodeEnum.SYS_SUCCESS.getMsg());
                return response;
            }
            response.setCode(ResponseCodeEnum.DATA_SAVE_ERROR.getCode());
            response.setMsg(ResponseCodeEnum.DATA_SAVE_ERROR.getMsg());
            return response;
        } catch (DuplicateKeyException e) {
            //TODO 用户名重复
        } catch (Exception e) {
            ServiceException serviceException = (ServiceException) ExceptionUtil.handlerException4biz(e);
            response.setCode(serviceException.getErrorCode());
            response.setMsg(serviceException.getErrorMessage());
        } finally {
            logger.info("register response:【" + response + "】");
        }

        return response;
    }

    private void beforeRegisterValidate(UserRegisterRequest request) {

        if (null == request) {
            throw new ValidateException("请求对象为空");
        }
        if (StringUtils.isEmpty(request.getUsername())) {
            throw new ValidateException("用户名为空");
        }
        if (StringUtils.isEmpty(request.getPassword())) {
            throw new ValidateException("密码为空");
        }
        if (StringUtils.isEmpty(request.getMobile())) {
            throw new ValidateException("密码为空");
        }
    }

}
