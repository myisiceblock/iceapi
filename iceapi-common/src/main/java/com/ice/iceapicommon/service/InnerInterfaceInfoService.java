package com.ice.iceapicommon.service;

import com.ice.iceapicommon.model.entity.InterfaceInfo;

/**
* @author zhang.
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2022-11-14 15:18:57
*/
public interface InnerInterfaceInfoService {

    /**
     * 从数据库中查询模拟接口是否存在（请求路径，请求方法，请求参数，布尔）
     * @return
     */
    InterfaceInfo getInterfaceInfo(String path,String method);
}
