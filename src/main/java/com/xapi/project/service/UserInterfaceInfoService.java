package com.xapi.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xapi.project.model.dto.userinterfaceinfo.UpdateUserInterfaceInfoDTO;
import com.xapi.xapicommon.model.entity.UserInterfaceInfo;

/**
 * 用户接口信息服务
 *
 * @author 15304
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 判断接口是否还有调用次数
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean isAvailable(long interfaceInfoId , long userId);


    /**
     * 更新用户的接口的调用次数
     * @param updateUserInterfaceInfoDTO
     * @return
     */
    boolean updateUserInterfaceInfo(UpdateUserInterfaceInfoDTO updateUserInterfaceInfoDTO);

}
