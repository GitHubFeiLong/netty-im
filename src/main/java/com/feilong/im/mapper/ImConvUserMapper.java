package com.feilong.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feilong.im.entity.ImConvUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feilong.im.dto.bo.ImConvUserBO;
import com.feilong.im.dto.page.query.ImConvUserPageQuery;

/**
 * im用户会话表 Mapper 接口
 * @author cfl 2026/04/16
 */
public interface ImConvUserMapper extends BaseMapper<ImConvUser> {


    /**
     * im用户会话表 分页查询
     * @param page 分页参数
     * @param pageQuery 查询参数
     * @return im用户会话表分页结果
     */
    Page<ImConvUserBO> page(Page<ImConvUserBO> page, ImConvUserPageQuery pageQuery);
}
