package com.feilong.im.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.feilong.im.core.MyJsonView;
import com.feilong.im.core.Result;
import com.feilong.im.dto.AuthenticationTokenDTO;
import com.feilong.im.dto.AuthenticationUserDetailsDTO;
import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.dto.form.ImSignInForm;
import com.feilong.im.dto.form.SysSignInForm;
import com.feilong.im.dto.req.ImSignUpReq;
import com.feilong.im.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author cfl 2026/04/13
 */
@Slf4j
@Tag(name = "登录注册")
@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService loginRegisterService;

    @PostMapping("/im/sign-up")
    @Operation(summary = "IM注册")
    @JsonView(MyJsonView.Simple.class)
    public Result<ImUserDTO> imSignUp(@Valid @RequestBody ImSignUpReq req) {
        return Result.ofSuccess(loginRegisterService.imSignUp(req));
    }

    @PostMapping("/im/sign-in")
    @Operation(summary = "IM登录")
    public Result<AuthenticationTokenDTO> imSignIn(@Valid @RequestBody ImSignInForm req) {
        return Result.ofSuccess(loginRegisterService.imSignIn(req));
    }

    @PostMapping("/sys/sign-in")
    @Operation(summary = "SYS登录")
    public Result<AuthenticationTokenDTO> sysSignIn(@Valid @RequestBody SysSignInForm req) {
        return Result.ofSuccess(loginRegisterService.sysSignIn(req));
    }

    @GetMapping("/user-details")
    @Operation(summary = "获取登录信息")
    public Result<AuthenticationUserDetailsDTO> getUserDetails() {
        return Result.ofSuccess(loginRegisterService.getUserDetails());
    }

    @PostMapping("/refresh")
    @Operation(summary = "刷新TOKEN")
    public Result<AuthenticationTokenDTO> refresh() {
        return Result.ofSuccess(loginRegisterService.refresh());
    }

    @PostMapping("/sign-out")
    @Operation(summary = "退出登录")
    public Result<Boolean> signOut() {
        return Result.ofSuccess(loginRegisterService.signOut());
    }
}
