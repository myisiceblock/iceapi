package com.iceblock.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ice.iceapicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author zhang.
* @description 针对表【user_interface_info】的数据库操作Mapper
* @createDate 2022-12-11 15:12:04
* @Entity com.iceblock.project.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




