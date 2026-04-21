package com.feilong.im.dto;

import com.feilong.im.dto.page.query.BasePageQuery;
import lombok.Data;

/**
 * @see BasePageQuery
 * @author cfl 2026/04/07
 */
@Deprecated
@Data
public class BasePage {

    /**
     * 页码（从1开始）
     */
    protected Integer pageNum;

    /**
     * 每页数量
     */
    protected Integer pageSize;
}
