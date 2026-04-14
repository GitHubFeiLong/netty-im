package com.feilong.im.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cfl 2026/04/14
 */
@Tag(name = "IM好友")
@Slf4j
@RestController
@RequestMapping("/im/friend")
@RequiredArgsConstructor
public class ImFriendController {

    // @Operation(summary = "添加好友")
    // @PostMapping
    // public Result<ImFriendDTO> addFriend() {
    //     return null;
    // }

}
