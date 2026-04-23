package com.feilong.im.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.feilong.im.entity.SysToken;
import com.feilong.im.dto.SysTokenDTO;
import com.feilong.im.dto.bo.SysTokenBO;
import com.feilong.im.dto.vo.SysTokenVO;
import com.feilong.im.dto.form.SysTokenForm;
import com.feilong.im.dto.form.SysTokenSaveForm;
import com.feilong.im.dto.form.SysTokenUpdateForm;
import com.feilong.im.dto.page.query.SysTokenPageQuery;
import org.springframework.web.bind.annotation.*;
import com.feilong.im.service.SysTokenService;
import com.feilong.im.mapstruct.SysTokenEntityMapper;
import com.feilong.im.core.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

/**
 * 认证用户TOKEN前端控制器
 * @author cfl 2026/04/23
 */
@Slf4j
@Tag(name = "认证用户TOKEN接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys-token")
public class SysTokenController {

    private final SysTokenService sysTokenService;
    private final SysTokenEntityMapper sysTokenEntityMapper;

    @Operation(summary = "认证用户TOKEN分页列表")
    @PostMapping("/page")
    public Result<IPage<SysTokenVO>> page(@RequestBody @Valid SysTokenPageQuery queryParams) {
        IPage<SysTokenVO> result = sysTokenService.page(queryParams);
        return Result.ofSuccess(result);
    }

    @Operation(summary = "认证用户TOKEN详细数据")
    @GetMapping("/{id}")
    public Result<SysTokenVO> getVO(@Parameter(description = "认证用户TOKENID") @PathVariable Long id) {
        SysToken entity = sysTokenService.getById(id);
        return Result.ofSuccess(sysTokenEntityMapper.toVo(entity));
    }

    @Operation(summary = "认证用户TOKEN表单数据")
    @GetMapping("/{id}/form")
    public Result<SysTokenForm> getForm(@Parameter(description = "认证用户TOKENID") @PathVariable Long id) {
        SysTokenForm formData = sysTokenService.getForm(id);
        return Result.ofSuccess(formData);
    }

    @Operation(summary = "新增认证用户TOKEN")
    @PostMapping
    public Result<SysTokenVO> save(@RequestBody @Valid SysTokenSaveForm formData) {
        SysToken sysToken = sysTokenService.save(formData);
        return Result.ofSuccess(sysTokenEntityMapper.toVo(sysToken));
    }

    @Operation(summary = "修改认证用户TOKEN")
    @PutMapping(value = "/{id}")
    public Result<SysTokenVO> update(@Parameter(description = "认证用户TOKENID") @PathVariable Long id, @RequestBody @Valid SysTokenUpdateForm formData) {
        SysToken sysToken = sysTokenService.update(id, formData);
        return Result.ofSuccess(sysTokenEntityMapper.toVo(sysToken));
    }

    @Operation(summary = "删除认证用户TOKEN")
    @DeleteMapping("/{ids}")
    public Result<Boolean> delete(@Parameter(description = "认证用户TOKENID，多个以英文逗号(,)分割") @PathVariable String ids) {
        boolean result = sysTokenService.delete(ids);
        return Result.ofJudge(result);
    }
}
