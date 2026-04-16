package com.feilong.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feilong.im.entity.SysAuthTokenBlacklist;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feilong.im.dto.bo.SysAuthTokenBlacklistBO;
import com.feilong.im.dto.page.query.SysAuthTokenBlacklistPageQuery;

/**
 * 认证token 黑名单 Mapper 接口
 * @author cfl 2026/04/16
 */
public interface SysAuthTokenBlacklistMapper extends BaseMapper<SysAuthTokenBlacklist> {


    /**
     * 认证token 黑名单 分页查询
     * @param page 分页参数
     * @param pageQuery 查询参数
     * @return 认证token 黑名单分页结果
     */
    Page<SysAuthTokenBlacklistBO> page(Page<SysAuthTokenBlacklistBO> page, SysAuthTokenBlacklistPageQuery pageQuery);
}
