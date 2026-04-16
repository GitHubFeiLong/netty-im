package com.feilong.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feilong.im.entity.ImUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feilong.im.dto.bo.ImUserBO;
import com.feilong.im.dto.page.query.ImUserPageQuery;

/**
 * im账户表 Mapper 接口
 * @author cfl 2026/04/16
 */
public interface ImUserMapper extends BaseMapper<ImUser> {


    /**
     * im账户表 分页查询
     * @param page 分页参数
     * @param pageQuery 查询参数
     * @return im账户表分页结果
     */
    Page<ImUserBO> page(Page<ImUserBO> page, ImUserPageQuery pageQuery);
}
