package com.xapi.xapicommon.service;

import com.xapi.xapicommon.model.entity.User;

/**
 * 用户服务
 * @author 15304
 */
public interface InnerUserService {

    /**
     * 数据库中查是否分配给用户密钥 ak
     * @param accessKey
     * @return
     */
    User getInvokerUser(String accessKey);



}
