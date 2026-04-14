package com.feilong.im.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feilong.im.mapper.ImFriendMapper;
import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.entity.ImFriend;
import com.feilong.im.entity.ImUser;
import com.feilong.im.handler.cmd.req.ContactPageReq;
import com.feilong.im.mapstruct.ImUserEntityMapper;
import com.feilong.im.service.ImFriendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author cfl 2026/03/31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImFriendServiceImpl extends ServiceImpl<ImFriendMapper, ImFriend> implements ImFriendService {
    private final ImUserEntityMapper imUserEntityMapper;
    private final ImFriendMapper imFriendMapper;

    /**
     * 查询用户联系人列表
     *
     * @param imUserId 用户ID
     * @param req  分页参数
     * @return 联系人列表
     */
    @Override
    public Page<ImUserDTO> listPage(Long imUserId, ContactPageReq req) {
        Page page = new Page(req.getPageNum(), req.getPageSize());
        Page<ImUser> imUserPage = imFriendMapper.listPage(page, imUserId, req);
        Page<ImUserDTO> imUserDTOPage = PageDTO.of(imUserPage.getCurrent(), imUserPage.getSize(), imUserPage.getTotal(), true);
        imUserDTOPage.setRecords(imUserEntityMapper.toDto(imUserPage.getRecords()));
        return imUserDTOPage;
    }
}
