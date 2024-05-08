package com.xapi.project.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户的ak，sk视图
 * @author 15304
 */
@Data
public class UserDevKeyVO implements Serializable {

    private static final long serialVersionUID = 6703326011663561616L;

    private String accessKey;

    private String secretKey;

}
