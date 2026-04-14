package com.feilong.im.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.feilong.im.core.MyJsonView;
import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.dto.req.ImSignUpReq;
import com.feilong.im.service.ImUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * im账户表 前端控制器
 * </p>
 *
 * @author author
 * @since 2026-03-18
 */
@Slf4j
@RestController
@RequestMapping("/im-user")
@RequiredArgsConstructor
public class ImUserController {

    private final ImUserService imUserService;


}
