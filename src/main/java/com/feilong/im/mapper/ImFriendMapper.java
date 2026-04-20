package com.feilong.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feilong.im.entity.ImFriend;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feilong.im.dto.bo.ImFriendBO;
import com.feilong.im.dto.page.query.ImFriendPageQuery;
import com.feilong.im.entity.ImUser;
import com.feilong.im.handler.netty.cmd.req.ContactPageReq;
import org.apache.ibatis.annotations.Param;

/**
 * 用户好友表 Mapper 接口
 * @author cfl 2026/04/16
 */
public interface ImFriendMapper extends BaseMapper<ImFriend> {


    /**
     * 用户好友表 分页查询
     * @param page 分页参数
     * @param pageQuery 查询参数
     * @return 用户好友表分页结果
     */
    Page<ImFriendBO> page(Page<ImFriendBO> page, ImFriendPageQuery pageQuery);

    /**
     * 查询用户联系人列表
     * @param imUserId 用户ID
     * @param pageReq 分页参数
     * @return
     */
    Page<ImUser> listPage(Page<ImUser> page, @Param("imUserId") Long imUserId, ContactPageReq pageReq);
}
