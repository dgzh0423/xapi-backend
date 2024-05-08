package com.xapi.project.service.impl.inner;

import com.xapi.project.service.UserInterfaceInfoService;
import com.xapi.xapicommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author 15304
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;


    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }

    @Override
    public boolean isAvailable(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.isAvailable(interfaceInfoId, userId);
    }
}
