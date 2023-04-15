package com.iceblock.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.iceapicommon.model.entity.UserInterfaceInfo;
import com.iceblock.project.common.ErrorCode;
import com.iceblock.project.exception.BusinessException;
import com.iceblock.project.mapper.UserInterfaceInfoMapper;
import com.iceblock.project.service.UserInterfaceInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhang.
 * @description 针对表【user_interface_info】的数据库操作Service实现
 * @createDate 2022-12-11 15:12:04
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 创建时，所有参数必须非空
        if (add) {
            if (userInterfaceInfo.getInterfaceInfoId() <= 0 || userInterfaceInfo.getUserId() <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口或用户不存在");
            }
        }
        if (userInterfaceInfo.getLeftNum() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数不能小于0");
        }
    }

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaUpdateWrapper<UserInterfaceInfo> updateWrapper = Wrappers.<UserInterfaceInfo>lambdaUpdate()
                .eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId)
                .eq(UserInterfaceInfo::getUserId, userId)
                .gt(UserInterfaceInfo::getLeftNum, 0)
                .setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
        return this.update(updateWrapper);
    }

    @Override
    public boolean getInvokeCountByUserId(long userId) {
        if(userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = this.getOne(Wrappers.<UserInterfaceInfo>lambdaQuery().eq(UserInterfaceInfo::getUserId, userId));
        return userInterfaceInfo.getLeftNum() > 0;
    }

    @Override
    public List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit) {
        List<UserInterfaceInfo> userInterfaceInfos = baseMapper.listTopInvokeInterfaceInfo(limit);
        return userInterfaceInfos;
    }
}




