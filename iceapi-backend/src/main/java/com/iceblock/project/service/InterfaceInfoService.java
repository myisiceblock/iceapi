package com.iceblock.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.iceapicommon.model.entity.InterfaceInfo;

/**
* @author zhang.
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2022-11-14 15:18:57
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 校验
     *
     * @param post
     * @param add 是否为创建校验
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
