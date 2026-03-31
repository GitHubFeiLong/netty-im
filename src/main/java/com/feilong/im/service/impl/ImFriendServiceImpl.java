package com.feilong.im.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feilong.im.dao.ImFriendMapper;
import com.feilong.im.entity.ImFriend;
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

}
