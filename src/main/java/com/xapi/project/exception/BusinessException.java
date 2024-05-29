package com.xapi.project.exception;

import com.xapi.project.common.ErrorCode;
import lombok.Getter;

/**
 * 自定义异常类
 * <a href="https://javaguide.cn/system-design/framework/spring/spring-common-annotations.html#_7-%E5%85%A8%E5%B1%80%E5%A4%84%E7%90%86-controller-%E5%B1%82%E5%BC%82%E5%B8%B8">...</a>
 *
 * @author 15304
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

}
