package com.xapi.project.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户的ak，sk视图
 * @author 15304
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDevKeyVO implements Serializable {

    private static final long serialVersionUID = 6703326011663561616L;

    @ExcelProperty
    private String accessKey;

    @ExcelProperty
    private String secretKey;

}
