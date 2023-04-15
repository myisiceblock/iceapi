package com.iceblock.project.service;

import com.ice.iceapicommon.service.InnerUserInterfaceInfoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class InnerUserInterfaceInfoServiceTest {

    @Resource
    private InnerUserInterfaceInfoService userInterfaceInfoService;

    @Test
    public void testInvokeCount() {
        boolean b = userInterfaceInfoService.invokeCount(31, 3);
        Assertions.assertTrue(b);
    }

}