package com.xapi.xapicommon.service;

/**
 * @author 15304
 *
*/
public interface InnerUserInterfaceInfoService {

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
}
