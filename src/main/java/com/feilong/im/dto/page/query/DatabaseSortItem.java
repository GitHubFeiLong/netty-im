package com.feilong.im.dto.page.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库排序项
 * @author cfl 2026/4/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseSortItem {
    @Schema(description = "排序字段")
    private String field;

    @Schema(description = "排序方式")
    private DatabaseSortOrderEnum order;

    /**
     * 排序方式
     * @author cfl 2026/4/14
     */
    public enum DatabaseSortOrderEnum {
        /**
         * 升序
         */
        ASC,
        /**
         * 降序
         */
        DESC
    }
}
