package com.iceblock.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.iceapicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
 * @author zhang.
 * @description 针对表【user_interface_info】的数据库操作Service
 * @createDate 2022-12-11 15:12:04
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean b);

    /**
     * 调用接口统计
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 获取用户是否还有调用次数
     * @param userId
     * @return
     */
    boolean getInvokeCountByUserId(long userId);

    /**
     * 获取热点接口
     * @param limit
     * @return
     */
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}
