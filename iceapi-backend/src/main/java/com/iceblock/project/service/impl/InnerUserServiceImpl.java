package com.iceblock.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ice.iceapicommon.model.entity.User;
import com.ice.iceapicommon.service.InnerUserService;
import com.iceblock.project.common.ErrorCode;
import com.iceblock.project.exception.BusinessException;
import com.iceblock.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserService userService;

    @Override
    public User getInvokeUser(String accessKey) {
        if(StringUtils.isAnyBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getAccessKey, accessKey));
        return user;
    }
}
