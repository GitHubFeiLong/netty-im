package com.feilong.im.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.feilong.im.config.security.token.AuthenticationToken;
import com.feilong.im.core.MyJsonView;
import com.feilong.im.core.Result;
import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.dto.req.ImLoginReq;
import com.feilong.im.dto.req.ImSignUpReq;
import com.feilong.im.service.LoginRegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cfl 2026/04/13
 */
@Slf4j
@Tag(name = "登录注册")
@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class LoginRegisterController {

    private final LoginRegisterService loginRegisterService;

    @PostMapping("/im/login")
    @Operation(summary = "IM登录")
    public Result<AuthenticationToken> imLogin(@Valid @RequestBody ImLoginReq req) {
        return Result.ofSuccess(loginRegisterService.imLogin(req));
    }

    @PostMapping("/im/sign-up")
    @Operation(summary = "IM注册")
    @JsonView(MyJsonView.Simple.class)
    public Result<ImUserDTO> imSignUp(@Valid @RequestBody ImSignUpReq req) {
        return Result.ofSuccess(loginRegisterService.imSignUp(req));
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    public Result<Boolean> logout() {
        return Result.ofSuccess(loginRegisterService.logout());
    }
}
