package com.feilong.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feilong.im.entity.ImConv;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feilong.im.dto.bo.ImConvBO;
import com.feilong.im.dto.page.query.ImConvPageQuery;

/**
 * im会话表 Mapper 接口
 * @author cfl 2026/04/16
 */
public interface ImConvMapper extends BaseMapper<ImConv> {


    /**
     * im会话表 分页查询
     * @param page 分页参数
     * @param pageQuery 查询参数
     * @return im会话表分页结果
     */
    Page<ImConvBO> page(Page<ImConvBO> page, ImConvPageQuery pageQuery);
}
