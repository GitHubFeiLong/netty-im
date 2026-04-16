package com.feilong.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feilong.im.entity.ImMessage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feilong.im.dto.bo.ImMessageBO;
import com.feilong.im.dto.page.query.ImMessagePageQuery;

/**
 * im消息表 Mapper 接口
 * @author cfl 2026/04/16
 */
public interface ImMessageMapper extends BaseMapper<ImMessage> {


    /**
     * im消息表 分页查询
     * @param page 分页参数
     * @param pageQuery 查询参数
     * @return im消息表分页结果
     */
    Page<ImMessageBO> page(Page<ImMessageBO> page, ImMessagePageQuery pageQuery);
}
