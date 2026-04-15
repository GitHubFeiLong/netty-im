package ${package.Form};

<#list importEntityPackages as pkg>
import ${pkg};
</#list>

import java.io.Serializable;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * ${table.comment} Form
 * @see ${package.Entity}.${entity}
 * @author ${author} ${date}
 */
@Data
@Accessors(chain = true)
@Schema(description = "${entity}Form")
public class ${entity}Form implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
<#----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
<#if field.keyFlag>
    <#assign keyPropertyName="${field.propertyName}"/>
</#if>

<#if field.comment!?length gt 0>
    <#if entityFieldUseJavaDoc>
    /**
     * ${field.comment}
     */
    </#if>
</#if>
    @Schema(description = "${field.comment}")
    private ${field.propertyType} ${field.propertyName};
</#list>
}
