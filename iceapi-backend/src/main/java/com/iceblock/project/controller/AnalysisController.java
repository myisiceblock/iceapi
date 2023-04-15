package com.iceblock.project.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ice.iceapicommon.model.entity.InterfaceInfo;
import com.ice.iceapicommon.model.entity.UserInterfaceInfo;
import com.iceblock.project.annotation.AuthCheck;
import com.iceblock.project.common.BaseResponse;
import com.iceblock.project.common.ErrorCode;
import com.iceblock.project.common.ResultUtils;
import com.iceblock.project.exception.BusinessException;
import com.iceblock.project.model.vo.InterfaceInfoVO;
import com.iceblock.project.service.InterfaceInfoService;
import com.iceblock.project.service.UserInterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<InterfaceInfoVO>> listTopInterfaceInvoke() {
        List<UserInterfaceInfo> userInterfaceInfos = userInterfaceInfoService.listTopInvokeInterfaceInfo(3);
        Map<Long, List<UserInterfaceInfo>> interfaceInfoIdObjMap = userInterfaceInfos.stream().collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        List<InterfaceInfo> interfaceInfos = interfaceInfoService.list(Wrappers.<InterfaceInfo>lambdaQuery().in(InterfaceInfo::getId, interfaceInfoIdObjMap.keySet()));
        if (CollectionUtils.isEmpty(interfaceInfos)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        List<InterfaceInfoVO> interfaceInfoVOS = interfaceInfos.stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
            int totalNum = interfaceInfoIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
            interfaceInfoVO.setTotalNum(totalNum);
            return interfaceInfoVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(interfaceInfoVOS);
    }
}
