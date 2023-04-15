package com.iceblock.project.service.impl;

import com.ice.iceapicommon.service.InnerUserInterfaceInfoService;
import com.iceblock.project.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }

    @Override
    public boolean getInvokeCountByUserId(long userId) {
        return userInterfaceInfoService.getInvokeCountByUserId(userId);
    }
}
