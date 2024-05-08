package com.xapi.xapicommon.service;

import com.xapi.xapicommon.model.entity.InterfaceInfo;

/**
 * @author 15304
 *
*/
public interface InnerInterfaceInfoService {

    /**
     * 数据库中查询模拟接口是否存在（请求路径，请求方法，请求参数）
     * @param url
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String url, String method);
}
