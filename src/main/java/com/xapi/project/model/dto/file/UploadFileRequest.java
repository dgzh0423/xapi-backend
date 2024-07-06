package com.xapi.project.model.dto.file;

import lombok.Data;

import java.io.Serializable;

/**
 * 文件上传请求
 * @author 15304
 */
@Data
public class UploadFileRequest implements Serializable {

    /**
     * 业务名称，例如 user_avatar
     */
    private String biz;

    private static final long serialVersionUID = 1L;
}