package com.xapi.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xapi.xapicommon.model.entity.InterfaceInfo;

/**
* @author 15304
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-11-07 21:27:32
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
