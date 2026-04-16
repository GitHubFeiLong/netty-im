package com.feilong.im.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.feilong.im.entity.ImUser;
import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.dto.bo.ImUserBO;
import com.feilong.im.dto.vo.ImUserVO;
import com.feilong.im.dto.form.ImUserForm;
import com.feilong.im.dto.page.query.ImUserPageQuery;
import org.springframework.web.bind.annotation.*;
import com.feilong.im.service.ImUserService;
import com.feilong.im.mapstruct.ImUserEntityMapper;
import com.feilong.im.core.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

/**
 * im账户表前端控制器
 * @author cfl 2026/04/16
 */
@Slf4j
@Tag(name = "im账户表接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/im-user")
public class ImUserController {

    private final ImUserService imUserService;
    private final ImUserEntityMapper imUserEntityMapper;

    @Operation(summary = "im账户表分页列表")
    @PostMapping("/page")
    public Result<IPage<ImUserVO>> page(@RequestBody @Valid ImUserPageQuery queryParams) {
        IPage<ImUserVO> result = imUserService.page(queryParams);
        return Result.ofSuccess(result);
    }

    @Operation(summary = "新增im账户表")
    @PostMapping
    public Result<ImUserVO> save(@RequestBody @Valid ImUserForm formData) {
        ImUser imUser = imUserService.save(formData);
        return Result.ofSuccess(imUserEntityMapper.toVo(imUser));
    }

    @Operation(summary = "im账户表详细数据")
    @GetMapping("/{id}")
    public Result<ImUserVO> getVO(@Parameter(description = "im账户表ID") @PathVariable Long id) {
        ImUser entity = imUserService.getById(id);
        return Result.ofSuccess(imUserEntityMapper.toVo(entity));
    }

    @Operation(summary = "im账户表表单数据")
    @GetMapping("/{id}/form")
    public Result<ImUserForm> getForm(@Parameter(description = "im账户表ID") @PathVariable Long id) {
        ImUserForm formData = imUserService.getForm(id);
        return Result.ofSuccess(formData);
    }

    @Operation(summary = "修改im账户表")
    @PutMapping(value = "/{id}")
    public Result<ImUserVO> update(@Parameter(description = "im账户表ID") @PathVariable Long id, @RequestBody @Valid ImUserForm formData) {
        ImUser imUser = imUserService.update(id, formData);
        return Result.ofSuccess(imUserEntityMapper.toVo(imUser));
    }

    @Operation(summary = "删除im账户表")
    @DeleteMapping("/{ids}")
    public Result<Boolean> delete(@Parameter(description = "im账户表ID，多个以英文逗号(,)分割") @PathVariable String ids) {
        boolean result = imUserService.delete(ids);
        return Result.ofJudge(result);
    }
}
