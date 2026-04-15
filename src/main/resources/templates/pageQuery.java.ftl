package ${package.PageQuery};

import java.io.Serializable;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import io.swagger.v3.oas.annotations.media.Schema;
import ${package.PageQuery}.BasePageQuery;

/**
 * ${table.comment} PageQuery
 * @see ${package.Entity}.${entity}
 * @author ${author} ${date}
 */
@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@Schema(description = "${entity}PageQuery")
public class ${entity}PageQuery extends BasePageQuery implements Serializable {

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

    @Schema(description = "分页开始时间")
    private LocalDateTime startTime;

    @Schema(description = "分页结束时间")
    private LocalDateTime endTime;

    /**
     * 获取开始时间
     *
     * @return LocalDateTime 当日0点
     */
    public LocalDateTime getStartTime() {
        return startTime == null ? null : startTime.toLocalDate().atStartOfDay();
    }

    /**
     * 获取截止时间
     *
     * @return LocalDateTime 当日最晚点
     */
    public LocalDateTime getEndTime() {
        return endTime == null ? null : endTime.toLocalDate().atTime(LocalTime.MAX);
    }
}
