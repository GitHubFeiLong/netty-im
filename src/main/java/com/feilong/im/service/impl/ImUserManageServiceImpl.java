package com.feilong.im.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.feilong.im.entity.ImConvUser;
import com.feilong.im.entity.ImFriend;
import com.feilong.im.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author cfl 2026/03/02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImUserManageServiceImpl implements ImUserManageService {

    private final ImUserService imUserService;
    private final ImConvService imConvService;
    private final ImConvUserService imConvUserService;
    private final ImFriendService imFriendService;

    /**
     * 查询用户所有好友ID
     *
     * @param userId 用户ID
     * @return 好友ID集合
     */
    @Override
    public List<Long> listFriendIds(Long userId) {
        return imFriendService.lambdaQuery().select(ImFriend::getFriendId).eq(ImFriend::getUserId, userId)
                .list()
                .stream()
                .map(ImFriend::getFriendId)
                .collect(Collectors.toList());
    }

    /**
     * 查询用户所有单聊会话的对方ID
     *
     * @param userId 用户ID
     * @return 对方ID集合
     */
    @Deprecated
    @Override
    public Set<Long> queryUserSingleSessionPartnerIds(Long userId) {

        return Collections.emptySet();
    }
}
