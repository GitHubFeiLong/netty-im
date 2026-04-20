package com.feilong.im.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.feilong.im.core.MyJsonView;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.feilong.im.entity.SysUser;
import com.feilong.im.dto.SysUserDTO;
import com.feilong.im.dto.bo.SysUserBO;
import com.feilong.im.dto.vo.SysUserVO;
import com.feilong.im.dto.form.SysUserForm;
import com.feilong.im.dto.form.SysUserSaveForm;
import com.feilong.im.dto.form.SysUserUpdateForm;
import com.feilong.im.dto.page.query.SysUserPageQuery;
import org.springframework.web.bind.annotation.*;
import com.feilong.im.service.SysUserService;
import com.feilong.im.mapstruct.SysUserEntityMapper;
import com.feilong.im.core.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

/**
 * 系统用户前端控制器
 * @author cfl 2026/04/16
 */
@Slf4j
@Tag(name = "系统用户接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys-user")
public class SysUserController {

    private final SysUserService sysUserService;
    private final SysUserEntityMapper sysUserEntityMapper;

    @Operation(summary = "系统用户分页列表")
    @PostMapping("/page")
    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(MyJsonView.Simple.class)
    public Result<IPage<SysUserVO>> page(@RequestBody @Valid SysUserPageQuery queryParams) {
        IPage<SysUserVO> result = sysUserService.page(queryParams);
        return Result.ofSuccess(result);
    }

    @Operation(summary = "系统用户详细数据")
    @GetMapping("/{id}")
    @JsonView(MyJsonView.Simple.class)
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public Result<SysUserVO> getVO(@Parameter(description = "系统用户ID") @PathVariable Long id) {
        SysUser entity = sysUserService.getById(id);
        return Result.ofSuccess(sysUserEntityMapper.toVo(entity));
    }

    @Operation(summary = "系统用户表单数据")
    @JsonView(MyJsonView.Simple.class)
    @GetMapping("/{id}/form")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public Result<SysUserForm> getForm(@Parameter(description = "系统用户ID") @PathVariable Long id) {
        SysUserForm formData = sysUserService.getForm(id);
        return Result.ofSuccess(formData);
    }

    @Operation(summary = "新增系统用户")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(MyJsonView.Simple.class)
    public Result<SysUserVO> save(@RequestBody @Valid SysUserSaveForm formData) {
        SysUser sysUser = sysUserService.save(formData);
        return Result.ofSuccess(sysUserEntityMapper.toVo(sysUser));
    }

    @Operation(summary = "修改系统用户")
    @PutMapping(value = "/{id}")
    @JsonView(MyJsonView.Simple.class)
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public Result<SysUserVO> update(@Parameter(description = "系统用户ID") @PathVariable Long id, @RequestBody @Valid SysUserUpdateForm formData) {
        SysUser sysUser = sysUserService.update(id, formData);
        return Result.ofSuccess(sysUserEntityMapper.toVo(sysUser));
    }

    @Operation(summary = "删除系统用户")
    @DeleteMapping("/{ids}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> delete(@Parameter(description = "系统用户ID，多个以英文逗号(,)分割") @PathVariable String ids) {
        boolean result = sysUserService.delete(ids);
        return Result.ofJudge(result);
    }
}
