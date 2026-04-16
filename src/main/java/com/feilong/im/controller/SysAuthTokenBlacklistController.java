package com.feilong.im.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.feilong.im.entity.SysAuthTokenBlacklist;
import com.feilong.im.dto.SysAuthTokenBlacklistDTO;
import com.feilong.im.dto.bo.SysAuthTokenBlacklistBO;
import com.feilong.im.dto.vo.SysAuthTokenBlacklistVO;
import com.feilong.im.dto.form.SysAuthTokenBlacklistForm;
import com.feilong.im.dto.form.SysAuthTokenBlacklistSaveForm;
import com.feilong.im.dto.form.SysAuthTokenBlacklistUpdateForm;
import com.feilong.im.dto.page.query.SysAuthTokenBlacklistPageQuery;
import org.springframework.web.bind.annotation.*;
import com.feilong.im.service.SysAuthTokenBlacklistService;
import com.feilong.im.mapstruct.SysAuthTokenBlacklistEntityMapper;
import com.feilong.im.core.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

/**
 * 认证token 黑名单前端控制器
 * @author cfl 2026/04/16
 */
@Slf4j
@Tag(name = "认证token 黑名单接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys-auth-token-blacklist")
public class SysAuthTokenBlacklistController {

    private final SysAuthTokenBlacklistService sysAuthTokenBlacklistService;
    private final SysAuthTokenBlacklistEntityMapper sysAuthTokenBlacklistEntityMapper;

    @Operation(summary = "认证token 黑名单分页列表")
    @PostMapping("/page")
    public Result<IPage<SysAuthTokenBlacklistVO>> page(@RequestBody @Valid SysAuthTokenBlacklistPageQuery queryParams) {
        IPage<SysAuthTokenBlacklistVO> result = sysAuthTokenBlacklistService.page(queryParams);
        return Result.ofSuccess(result);
    }

    @Operation(summary = "认证token 黑名单详细数据")
    @GetMapping("/{id}")
    public Result<SysAuthTokenBlacklistVO> getVO(@Parameter(description = "认证token 黑名单ID") @PathVariable Long id) {
        SysAuthTokenBlacklist entity = sysAuthTokenBlacklistService.getById(id);
        return Result.ofSuccess(sysAuthTokenBlacklistEntityMapper.toVo(entity));
    }

    @Operation(summary = "认证token 黑名单表单数据")
    @GetMapping("/{id}/form")
    public Result<SysAuthTokenBlacklistForm> getForm(@Parameter(description = "认证token 黑名单ID") @PathVariable Long id) {
        SysAuthTokenBlacklistForm formData = sysAuthTokenBlacklistService.getForm(id);
        return Result.ofSuccess(formData);
    }

    @Operation(summary = "新增认证token 黑名单")
    @PostMapping
    public Result<SysAuthTokenBlacklistVO> save(@RequestBody @Valid SysAuthTokenBlacklistSaveForm formData) {
        SysAuthTokenBlacklist sysAuthTokenBlacklist = sysAuthTokenBlacklistService.save(formData);
        return Result.ofSuccess(sysAuthTokenBlacklistEntityMapper.toVo(sysAuthTokenBlacklist));
    }

    @Operation(summary = "修改认证token 黑名单")
    @PutMapping(value = "/{id}")
    public Result<SysAuthTokenBlacklistVO> update(@Parameter(description = "认证token 黑名单ID") @PathVariable Long id, @RequestBody @Valid SysAuthTokenBlacklistUpdateForm formData) {
        SysAuthTokenBlacklist sysAuthTokenBlacklist = sysAuthTokenBlacklistService.update(id, formData);
        return Result.ofSuccess(sysAuthTokenBlacklistEntityMapper.toVo(sysAuthTokenBlacklist));
    }

    @Operation(summary = "删除认证token 黑名单")
    @DeleteMapping("/{ids}")
    public Result<Boolean> delete(@Parameter(description = "认证token 黑名单ID，多个以英文逗号(,)分割") @PathVariable String ids) {
        boolean result = sysAuthTokenBlacklistService.delete(ids);
        return Result.ofJudge(result);
    }
}
