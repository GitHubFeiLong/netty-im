package com.feilong.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feilong.im.entity.SysUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feilong.im.dto.bo.SysUserBO;
import com.feilong.im.dto.page.query.SysUserPageQuery;

/**
 * 系统用户 Mapper 接口
 * @author cfl 2026/04/16
 */
public interface SysUserMapper extends BaseMapper<SysUser> {


    /**
     * 系统用户 分页查询
     * @param page 分页参数
     * @param pageQuery 查询参数
     * @return 系统用户分页结果
     */
    Page<SysUserBO> page(Page<SysUserBO> page, SysUserPageQuery pageQuery);
}
