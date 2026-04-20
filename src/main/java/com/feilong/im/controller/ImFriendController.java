package com.feilong.im.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.feilong.im.entity.ImFriend;
import com.feilong.im.dto.ImFriendDTO;
import com.feilong.im.dto.bo.ImFriendBO;
import com.feilong.im.dto.vo.ImFriendVO;
import com.feilong.im.dto.form.ImFriendForm;
import com.feilong.im.dto.form.ImFriendSaveForm;
import com.feilong.im.dto.form.ImFriendUpdateForm;
import com.feilong.im.dto.page.query.ImFriendPageQuery;
import org.springframework.web.bind.annotation.*;
import com.feilong.im.service.ImFriendService;
import com.feilong.im.mapstruct.ImFriendEntityMapper;
import com.feilong.im.core.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

/**
 * 用户好友表前端控制器
 * @author cfl 2026/04/16
 */
@Slf4j
@Tag(name = "用户好友表接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/im-friend")
public class ImFriendController {

    private final ImFriendService imFriendService;
    private final ImFriendEntityMapper imFriendEntityMapper;

    @Operation(summary = "用户好友表分页列表")
    @PostMapping("/page")
    public Result<IPage<ImFriendVO>> page(@RequestBody @Valid ImFriendPageQuery queryParams) {
        IPage<ImFriendVO> result = imFriendService.page(queryParams);
        return Result.ofSuccess(result);
    }

    @Operation(summary = "用户好友表详细数据")
    @GetMapping("/{id}")
    public Result<ImFriendVO> getVO(@Parameter(description = "用户好友表ID") @PathVariable Long id) {
        ImFriend entity = imFriendService.getById(id);
        return Result.ofSuccess(imFriendEntityMapper.toVo(entity));
    }

    @Operation(summary = "用户好友表表单数据")
    @GetMapping("/{id}/form")
    public Result<ImFriendForm> getForm(@Parameter(description = "用户好友表ID") @PathVariable Long id) {
        ImFriendForm formData = imFriendService.getForm(id);
        return Result.ofSuccess(formData);
    }

    @Operation(summary = "新增用户好友表")
    @PostMapping
    public Result<ImFriendVO> save(@RequestBody @Valid ImFriendSaveForm formData) {
        ImFriend imFriend = imFriendService.save(formData);
        return Result.ofSuccess(imFriendEntityMapper.toVo(imFriend));
    }

    @Operation(summary = "修改用户好友表")
    @PutMapping(value = "/{id}")
    public Result<ImFriendVO> update(@Parameter(description = "用户好友表ID") @PathVariable Long id, @RequestBody @Valid ImFriendUpdateForm formData) {
        ImFriend imFriend = imFriendService.update(id, formData);
        return Result.ofSuccess(imFriendEntityMapper.toVo(imFriend));
    }

    @Operation(summary = "删除用户好友表")
    @DeleteMapping("/{ids}")
    public Result<Boolean> delete(@Parameter(description = "用户好友表ID，多个以英文逗号(,)分割") @PathVariable String ids) {
        boolean result = imFriendService.delete(ids);
        return Result.ofJudge(result);
    }
}
