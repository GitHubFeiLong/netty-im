package ${package.Controller};

import org.springframework.web.bind.annotation.RequestMapping;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
<#list pojoPkgs as pkg>
import ${pkg};
</#list>
import org.springframework.web.bind.annotation.*;
import ${package.Service}.${entity}Service;
import ${package.EntityMapper}.${entity}EntityMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
/**
 * ${table.comment!} 前端控制器
 * @author ${author} ${date}
 */
@Slf4j
@Tag(name = "${table.comment}接口")
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequiredArgsConstructor
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
public class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
    <#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
    <#else>
public class ${table.controllerName} {
    </#if>

    private final ${entity}Service ${firstCharLowerCaseEntity}Service;
    private final ${entity}EntityMapper ${firstCharLowerCaseEntity}EntityMapper;

    @Operation(summary = "${table.comment}分页列表")
    @GetMapping("/page")
    public Result<IPage<${entity}VO>> page(@Valid ${entity}PageQuery queryParams) {
        IPage<${entity}VO> result = ${firstCharLowerCaseEntity}Service.page(queryParams);
        return Result.ofSuccess(result);
    }

    @Operation(summary = "新增${table.comment}")
    @PostMapping
    public Result<${entity}VO> save${entity}(@RequestBody @Valid ${entity}Form formData) {
        ${entity} ${firstCharLowerCaseEntity} = ${firstCharLowerCaseEntity}Service.save(formData);
        return Result.ofSuccess(${firstCharLowerCaseEntity}EntityMapper.toVo(${firstCharLowerCaseEntity}));
    }

    @Operation(summary = "${table.comment}详细数据")
    @GetMapping("/{id}")
    public Result<${entity}VO> get${entity}VO(@Parameter(description = "$!{table.comment}ID") @PathVariable Long id) {
        ${entity} entity = ${firstCharLowerCaseEntity}Service.getById(id);
        return Result.ofSuccess(${firstCharLowerCaseEntity}EntityMapper.toVo(entity));
    }

    @Operation(summary = "${table.comment}表单数据")
    @GetMapping("/{id}/form")
    public Result<${entity}Form> get${entity}Form(@Parameter(description = "$!{table.comment}ID") @PathVariable Long id) {
        ${entity}Form formData = ${firstCharLowerCaseEntity}Service.getForm(id);
        return Result.ofSuccess(formData);
    }

    @Operation(summary = "修改${table.comment}")
    @PutMapping(value = "/{id}")
    public Result<${entity}VO> update${entity}(@Parameter(description = "$!{table.comment}ID") @PathVariable Long id, @RequestBody @Valid ${entity}Form formData) {
        ${entity} ${firstCharLowerCaseEntity} = ${firstCharLowerCaseEntity}Service.update(id, formData);
        return Result.ofSuccess(${firstCharLowerCaseEntity}EntityMapper.toVo(${firstCharLowerCaseEntity}));
    }

    @Operation(summary = "删除${table.comment}")
    @DeleteMapping("/{ids}")
    public Result<Boolean> delete${entity}s(@Parameter(description = "$!{table.comment}ID，多个以英文逗号(,)分割") @PathVariable String ids) {
        boolean result = ${firstCharLowerCaseEntity}Service.delete(ids);
        return Result.ofJudge(result);
    }
}
</#if>
