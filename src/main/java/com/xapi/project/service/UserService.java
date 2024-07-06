package com.xapi.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xapi.project.enums.FileUploadBizEnum;
import com.xapi.project.model.vo.UserDevKeyVO;
import com.xapi.xapicommon.model.entity.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 * @author 15304
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 重新生成ak，sk
     * @param request
     * @return
     */
    UserDevKeyVO genkey(HttpServletRequest request);

    /**
     * 当前登录用户查看自己的ak,sk
     * @param request
     * @return
     */
    UserDevKeyVO getkey(HttpServletRequest request);

    /**
     * 校验用户头像
     * @param multipartFile
     * @param fileUploadBizEnum
     */
    void validAvatar(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum);

    /**
     * 保存用户头像的访问地址
     * @param userId
     * @param url
     * @return
     */
    boolean saveAvatar(Long userId, String url);
}
