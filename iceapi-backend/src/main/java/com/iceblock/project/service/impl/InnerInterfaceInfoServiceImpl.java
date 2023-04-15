package com.iceblock.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ice.iceapicommon.model.entity.InterfaceInfo;
import com.ice.iceapicommon.service.InnerInterfaceInfoService;
import com.iceblock.project.common.ErrorCode;
import com.iceblock.project.exception.BusinessException;
import com.iceblock.project.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        if(StringUtils.isAnyBlank(url,method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getOne(Wrappers.<InterfaceInfo>lambdaQuery()
                .eq(InterfaceInfo::getUrl, url).eq(InterfaceInfo::getMethod, method));
        return interfaceInfo;
    }
}
