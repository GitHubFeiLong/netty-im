package com.feilong.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feilong.im.entity.SysToken;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feilong.im.dto.bo.SysTokenBO;
import com.feilong.im.dto.page.query.SysTokenPageQuery;

/**
 * 认证用户TOKEN Mapper 接口
 * @author cfl 2026/04/23
 */
public interface SysTokenMapper extends BaseMapper<SysToken> {


    /**
     * 认证用户TOKEN 分页查询
     * @param page 分页参数
     * @param pageQuery 查询参数
     * @return 认证用户TOKEN分页结果
     */
    Page<SysTokenBO> page(Page<SysTokenBO> page, SysTokenPageQuery pageQuery);
}
