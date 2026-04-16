package com.feilong.im.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.feilong.im.entity.ImConvUser;
import com.feilong.im.dto.ImConvUserDTO;
import com.feilong.im.dto.bo.ImConvUserBO;
import com.feilong.im.dto.vo.ImConvUserVO;
import com.feilong.im.dto.form.ImConvUserForm;
import com.feilong.im.dto.page.query.ImConvUserPageQuery;
import org.springframework.web.bind.annotation.*;
import com.feilong.im.service.ImConvUserService;
import com.feilong.im.mapstruct.ImConvUserEntityMapper;
import com.feilong.im.core.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

/**
 * im用户会话表前端控制器
 * @author cfl 2026/04/16
 */
@Slf4j
@Tag(name = "im用户会话表接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/im-conv-user")
public class ImConvUserController {

    private final ImConvUserService imConvUserService;
    private final ImConvUserEntityMapper imConvUserEntityMapper;

    @Operation(summary = "im用户会话表分页列表")
    @PostMapping("/page")
    public Result<IPage<ImConvUserVO>> page(@RequestBody @Valid ImConvUserPageQuery queryParams) {
        IPage<ImConvUserVO> result = imConvUserService.page(queryParams);
        return Result.ofSuccess(result);
    }

    @Operation(summary = "新增im用户会话表")
    @PostMapping
    public Result<ImConvUserVO> save(@RequestBody @Valid ImConvUserForm formData) {
        ImConvUser imConvUser = imConvUserService.save(formData);
        return Result.ofSuccess(imConvUserEntityMapper.toVo(imConvUser));
    }

    @Operation(summary = "im用户会话表详细数据")
    @GetMapping("/{id}")
    public Result<ImConvUserVO> getVO(@Parameter(description = "im用户会话表ID") @PathVariable Long id) {
        ImConvUser entity = imConvUserService.getById(id);
        return Result.ofSuccess(imConvUserEntityMapper.toVo(entity));
    }

    @Operation(summary = "im用户会话表表单数据")
    @GetMapping("/{id}/form")
    public Result<ImConvUserForm> getForm(@Parameter(description = "im用户会话表ID") @PathVariable Long id) {
        ImConvUserForm formData = imConvUserService.getForm(id);
        return Result.ofSuccess(formData);
    }

    @Operation(summary = "修改im用户会话表")
    @PutMapping(value = "/{id}")
    public Result<ImConvUserVO> update(@Parameter(description = "im用户会话表ID") @PathVariable Long id, @RequestBody @Valid ImConvUserForm formData) {
        ImConvUser imConvUser = imConvUserService.update(id, formData);
        return Result.ofSuccess(imConvUserEntityMapper.toVo(imConvUser));
    }

    @Operation(summary = "删除im用户会话表")
    @DeleteMapping("/{ids}")
    public Result<Boolean> delete(@Parameter(description = "im用户会话表ID，多个以英文逗号(,)分割") @PathVariable String ids) {
        boolean result = imConvUserService.delete(ids);
        return Result.ofJudge(result);
    }
}
