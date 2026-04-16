package com.feilong.im.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.feilong.im.entity.ImMessage;
import com.feilong.im.dto.ImMessageDTO;
import com.feilong.im.dto.bo.ImMessageBO;
import com.feilong.im.dto.vo.ImMessageVO;
import com.feilong.im.dto.form.ImMessageForm;
import com.feilong.im.dto.form.ImMessageSaveForm;
import com.feilong.im.dto.form.ImMessageUpdateForm;
import com.feilong.im.dto.page.query.ImMessagePageQuery;
import org.springframework.web.bind.annotation.*;
import com.feilong.im.service.ImMessageService;
import com.feilong.im.mapstruct.ImMessageEntityMapper;
import com.feilong.im.core.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

/**
 * im消息表前端控制器
 * @author cfl 2026/04/16
 */
@Slf4j
@Tag(name = "im消息表接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/im-message")
public class ImMessageController {

    private final ImMessageService imMessageService;
    private final ImMessageEntityMapper imMessageEntityMapper;

    @Operation(summary = "im消息表分页列表")
    @PostMapping("/page")
    public Result<IPage<ImMessageVO>> page(@RequestBody @Valid ImMessagePageQuery queryParams) {
        IPage<ImMessageVO> result = imMessageService.page(queryParams);
        return Result.ofSuccess(result);
    }

    @Operation(summary = "im消息表详细数据")
    @GetMapping("/{id}")
    public Result<ImMessageVO> getVO(@Parameter(description = "im消息表ID") @PathVariable Long id) {
        ImMessage entity = imMessageService.getById(id);
        return Result.ofSuccess(imMessageEntityMapper.toVo(entity));
    }

    @Operation(summary = "im消息表表单数据")
    @GetMapping("/{id}/form")
    public Result<ImMessageForm> getForm(@Parameter(description = "im消息表ID") @PathVariable Long id) {
        ImMessageForm formData = imMessageService.getForm(id);
        return Result.ofSuccess(formData);
    }

    @Operation(summary = "新增im消息表")
    @PostMapping
    public Result<ImMessageVO> save(@RequestBody @Valid ImMessageSaveForm formData) {
        ImMessage imMessage = imMessageService.save(formData);
        return Result.ofSuccess(imMessageEntityMapper.toVo(imMessage));
    }

    @Operation(summary = "修改im消息表")
    @PutMapping(value = "/{id}")
    public Result<ImMessageVO> update(@Parameter(description = "im消息表ID") @PathVariable Long id, @RequestBody @Valid ImMessageUpdateForm formData) {
        ImMessage imMessage = imMessageService.update(id, formData);
        return Result.ofSuccess(imMessageEntityMapper.toVo(imMessage));
    }

    @Operation(summary = "删除im消息表")
    @DeleteMapping("/{ids}")
    public Result<Boolean> delete(@Parameter(description = "im消息表ID，多个以英文逗号(,)分割") @PathVariable String ids) {
        boolean result = imMessageService.delete(ids);
        return Result.ofJudge(result);
    }
}
