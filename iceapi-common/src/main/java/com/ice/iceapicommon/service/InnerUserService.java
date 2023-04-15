package com.ice.iceapicommon.service;


import com.ice.iceapicommon.model.entity.User;

/**
 * 用户服务
 *
 * @author zhang.
 */
public interface InnerUserService {

    /**
     * 数据库中查是否已分配用户密钥（accessKey,secretKey,布尔）
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);
}
