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
import ${importResult};
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

/**
 * ${table.comment!}前端控制器
 * @author ${author} ${date}
 */
@Slf4j
@Tag(name = "${table.comment!}接口")
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

    private final ${entity}Service ${entity?uncap_first}Service;
    private final ${entity}EntityMapper ${entity?uncap_first}EntityMapper;

    @Operation(summary = "${table.comment!}分页列表")
    @PostMapping("/page")
    public Result<IPage<${entity}VO>> page(@RequestBody @Valid ${entity}PageQuery queryParams) {
        IPage<${entity}VO> result = ${entity?uncap_first}Service.page(queryParams);
        return Result.ofSuccess(result);
    }

    @Operation(summary = "新增${table.comment!}")
    @PostMapping
    public Result<${entity}VO> save(@RequestBody @Valid ${entity}Form formData) {
        ${entity} ${entity?uncap_first} = ${entity?uncap_first}Service.save(formData);
        return Result.ofSuccess(${entity?uncap_first}EntityMapper.toVo(${entity?uncap_first}));
    }

    @Operation(summary = "${table.comment!}详细数据")
    @GetMapping("/{id}")
    public Result<${entity}VO> getVO(@Parameter(description = "${table.comment!}ID") @PathVariable Long id) {
        ${entity} entity = ${entity?uncap_first}Service.getById(id);
        return Result.ofSuccess(${entity?uncap_first}EntityMapper.toVo(entity));
    }

    @Operation(summary = "${table.comment!}表单数据")
    @GetMapping("/{id}/form")
    public Result<${entity}Form> getForm(@Parameter(description = "${table.comment!}ID") @PathVariable Long id) {
        ${entity}Form formData = ${entity?uncap_first}Service.getForm(id);
        return Result.ofSuccess(formData);
    }

    @Operation(summary = "修改${table.comment!}")
    @PutMapping(value = "/{id}")
    public Result<${entity}VO> update(@Parameter(description = "${table.comment!}ID") @PathVariable Long id, @RequestBody @Valid ${entity}Form formData) {
        ${entity} ${entity?uncap_first} = ${entity?uncap_first}Service.update(id, formData);
        return Result.ofSuccess(${entity?uncap_first}EntityMapper.toVo(${entity?uncap_first}));
    }

    @Operation(summary = "删除${table.comment!}")
    @DeleteMapping("/{ids}")
    public Result<Boolean> delete(@Parameter(description = "${table.comment!}ID，多个以英文逗号(,)分割") @PathVariable String ids) {
        boolean result = ${entity?uncap_first}Service.delete(ids);
        return Result.ofJudge(result);
    }
}
</#if>
