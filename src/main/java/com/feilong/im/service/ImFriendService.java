package com.feilong.im.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.entity.ImFriend;
import com.feilong.im.entity.ImUser;
import com.feilong.im.handler.cmd.req.ContactPageReq;

/**
 * @author cfl 2026/03/31
 */
public interface ImFriendService extends IService<ImFriend> {

    /**
     * 查询用户联系人列表
     * @param imUserId 用户ID
     * @param pageReq 分页参数
     * @return 联系人列表
     */
    Page<ImUserDTO> listPage(Long imUserId, ContactPageReq pageReq);

}
