package com.xapi.project.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求
 * @author 15304
 */
@Data
public class IdRequest implements Serializable {
    /**
     * 接口id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}