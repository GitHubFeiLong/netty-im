package com.feilong.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feilong.im.entity.ImFriend;
import com.feilong.im.entity.ImUser;
import com.feilong.im.handler.cmd.req.ContactPageReq;
import org.apache.ibatis.annotations.Param;

/**
 * @author cfl 2026/03/31
 */
public interface ImFriendMapper extends BaseMapper<ImFriend> {

    /**
     * 查询用户联系人列表
     * @param imUserId 用户ID
     * @param pageReq 分页参数
     * @return
     */
    Page<ImUser> listPage(Page page, @Param("imUserId") Long imUserId, ContactPageReq pageReq);
}
