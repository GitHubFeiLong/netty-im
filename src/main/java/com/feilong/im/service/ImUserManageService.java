package com.feilong.im.service;

import java.util.List;
import java.util.Set;

/**
 * @author cfl 2026/03/02
 */
public interface ImUserManageService {
    /**
     * 查询用户所有好友ID
     * @param userId 用户ID
     * @return 好友ID集合
     */
    List<Long> listFriendIds(Long userId);

    /**
     * 查询用户所有单聊会话的对方ID
     * @param userId 用户ID
     * @return 对方ID集合
     */
    Set<Long> queryUserSingleSessionPartnerIds(Long userId);

}
