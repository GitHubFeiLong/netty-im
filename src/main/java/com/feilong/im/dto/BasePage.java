package com.feilong.im.dto;

import lombok.Data;

/**
 * @author cfl 2026/04/07
 */
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
