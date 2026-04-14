package com.feilong.im.dto.page.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;


/**
 * 基础分页查询对象
 * @author cfl 2026/4/14
 */
@Data
public class BasePageQuery {

    /**
     * 导出时调用分页查询，有时不需要分页
     */
    @Schema(description = "是否开启分页", hidden = true)
    private Boolean openPage = true;

    @Schema(description = "第几页,从1开始")
    @NotNull(message = "分页查询page参数必传")
    @Min(value = 1L, message = "分页参数错误，page必须大于等于1")
    private Integer page = 1;

    @Schema(description = "一页显示内容长度")
    @NotNull(message = "分页查询size参数必传")
    @Min(value = 1L, message = "分页参数错误，size必须大于等于1")
    private Integer size = 10;

    @Schema(description = "排序字段")
    private List<DatabaseSortItem> sorts;

    /**
     * 分页起始序号
     *
     * @return 起始序号
     */
    public Long getStartSerialNumber() {
        return (long)(this.page - 1) * size + 1;
    }
}
