package com.xapi.project.model.dto.userinterfaceinfo;

import lombok.Data;
import java.io.Serializable;

/**
 * @author 15304
 */
@Data
public class UpdateUserInterfaceInfoDTO implements Serializable {


    private static final long serialVersionUID = 1472097902521779075L;

    /**
     * 申请用户（当前登录用户）
     */
    private Long userId;

    /**
     * 申请的接口ID
     */
    private Long interfaceId;

    /**
     * 申请的调用次数
     */
    private Integer invokeCount;
}
