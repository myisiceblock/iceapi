package com.ice.iceapicommon.service;

import com.ice.iceapicommon.model.entity.UserInterfaceInfo;

/**
 * @author zhang.
 * @description 针对表【user_interface_info】的数据库操作Service
 * @createDate 2022-12-11 15:12:04
 */
public interface InnerUserInterfaceInfoService {

    /**
     * 调用接口统计
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 获取调用次数
     * @param userId
     * @return
     */
    boolean getInvokeCountByUserId(long userId);
}
