package com.feilong.im.service;

import com.feilong.im.config.security.authentication.imuser.ImUserDetails;
import com.feilong.im.config.security.token.AuthenticationToken;
import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.dto.form.SysLoginForm;
import com.feilong.im.dto.req.ImLoginReq;
import com.feilong.im.dto.req.ImSignUpReq;
import jakarta.validation.Valid;

/**
 * @author cfl 2026/04/13
 */
public interface LoginRegisterService {

    /**
     * IM登录
     * @param req 登录参数
     * @return 登录结果
     */
    AuthenticationToken imLogin(@Valid ImLoginReq req);

    /**
     * IM注册
     * @param req 注册参数
     * @return 注册结果
     */
    ImUserDTO imSignUp(@Valid ImSignUpReq req);

    /**
     * 退出登录，将token置为无效
     * @return 退出结果
     */
    Boolean logout();

    /**
     * 系统用户登录
     * @param req 登录参数
     * @return 登录结果
     */
    AuthenticationToken sysLogin(@Valid SysLoginForm req);
}
