package com.feilong.im.service;

import com.feilong.im.dto.AuthenticationTokenDTO;
import com.feilong.im.dto.AuthenticationUserDetailsDTO;
import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.dto.form.SysLoginForm;
import com.feilong.im.dto.req.ImLoginReq;
import com.feilong.im.dto.req.ImSignUpReq;
import jakarta.validation.Valid;

/**
 * @author cfl 2026/04/13
 */
public interface AuthService {

    /**
     * IM登录
     * @param req 登录参数
     * @return 登录结果
     */
    AuthenticationTokenDTO imSignIn(@Valid ImLoginReq req);

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
    Boolean signOut();

    /**
     * 系统用户登录
     * @param req 登录参数
     * @return 登录结果
     */
    AuthenticationTokenDTO sysSignIn(@Valid SysLoginForm req);

    /**
     * 获取当前登录用户信息
     * @return 用户信息
     */
    AuthenticationUserDetailsDTO getUserDetails();

    /**
     * 刷新TOKEN
     * @return 刷新结果
     */
    AuthenticationTokenDTO refresh();


}
