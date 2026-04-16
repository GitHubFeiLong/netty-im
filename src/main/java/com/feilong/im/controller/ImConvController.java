package com.feilong.im.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.feilong.im.entity.ImConv;
import com.feilong.im.dto.ImConvDTO;
import com.feilong.im.dto.bo.ImConvBO;
import com.feilong.im.dto.vo.ImConvVO;
import com.feilong.im.dto.form.ImConvForm;
import com.feilong.im.dto.page.query.ImConvPageQuery;
import org.springframework.web.bind.annotation.*;
import com.feilong.im.service.ImConvService;
import com.feilong.im.mapstruct.ImConvEntityMapper;
import com.feilong.im.core.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

/**
 * im会话表前端控制器
 * @author cfl 2026/04/16
 */
@Slf4j
@Tag(name = "im会话表接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/im-conv")
public class ImConvController {

    private final ImConvService imConvService;
    private final ImConvEntityMapper imConvEntityMapper;

    @Operation(summary = "im会话表分页列表")
    @PostMapping("/page")
    public Result<IPage<ImConvVO>> page(@RequestBody @Valid ImConvPageQuery queryParams) {
        IPage<ImConvVO> result = imConvService.page(queryParams);
        return Result.ofSuccess(result);
    }

    @Operation(summary = "新增im会话表")
    @PostMapping
    public Result<ImConvVO> save(@RequestBody @Valid ImConvForm formData) {
        ImConv imConv = imConvService.save(formData);
        return Result.ofSuccess(imConvEntityMapper.toVo(imConv));
    }

    @Operation(summary = "im会话表详细数据")
    @GetMapping("/{id}")
    public Result<ImConvVO> getVO(@Parameter(description = "im会话表ID") @PathVariable Long id) {
        ImConv entity = imConvService.getById(id);
        return Result.ofSuccess(imConvEntityMapper.toVo(entity));
    }

    @Operation(summary = "im会话表表单数据")
    @GetMapping("/{id}/form")
    public Result<ImConvForm> getForm(@Parameter(description = "im会话表ID") @PathVariable Long id) {
        ImConvForm formData = imConvService.getForm(id);
        return Result.ofSuccess(formData);
    }

    @Operation(summary = "修改im会话表")
    @PutMapping(value = "/{id}")
    public Result<ImConvVO> update(@Parameter(description = "im会话表ID") @PathVariable Long id, @RequestBody @Valid ImConvForm formData) {
        ImConv imConv = imConvService.update(id, formData);
        return Result.ofSuccess(imConvEntityMapper.toVo(imConv));
    }

    @Operation(summary = "删除im会话表")
    @DeleteMapping("/{ids}")
    public Result<Boolean> delete(@Parameter(description = "im会话表ID，多个以英文逗号(,)分割") @PathVariable String ids) {
        boolean result = imConvService.delete(ids);
        return Result.ofJudge(result);
    }
}
