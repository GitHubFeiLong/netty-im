package ${package.VO};

import java.io.Serializable;
import java.io.Serial;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * ${table.comment} VO
 * @see ${package.Entity}.${entity}
 * @author ${author} ${date}
 */
@Data
@Schema(description = "${entity}VO")
public class ${entity}VO implements Serializable {

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
