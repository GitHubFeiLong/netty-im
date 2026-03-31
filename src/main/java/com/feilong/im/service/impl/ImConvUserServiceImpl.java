package com.feilong.im.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feilong.im.context.CurrentTimeContext;
import com.feilong.im.dao.ImConvUserMapper;
import com.feilong.im.entity.ImConvUser;
import com.feilong.im.service.ImConvUserService;
import com.feilong.im.service.ImMessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * im用户会话表 服务实现类
 * </p>
 *
 * @author author
 * @since 2026-02-25
 */
@Slf4j
@Service
@AllArgsConstructor
public class ImConvUserServiceImpl extends ServiceImpl<ImConvUserMapper, ImConvUser> implements ImConvUserService {

    private final ImMessageService imMessageService;

    /**
     * 获取或创建会话用户
     *
     * @param convId 会话ID
     * @param userId 用户ID
     * @return 会话用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImConvUser getOrCreate(Long convId, Long userId) {
        // 查询用户2的会话是否存在
        ImConvUser imConvUser = super.lambdaQuery().eq(ImConvUser::getConvId, convId).eq(ImConvUser::getUserId, userId).one();

        if (imConvUser == null) {
            // 查询未读消息数
            int countUnreadMessage = imMessageService.countUnreadMessage(convId, userId);
            LocalDateTime now = CurrentTimeContext.get();
            imConvUser = new ImConvUser();
            imConvUser.setConvId(convId);
            imConvUser.setUserId(userId);
            imConvUser.setUnreadCount(countUnreadMessage);
            imConvUser.setCreateTime(now);
            imConvUser.setUpdateTime(now);

            super.save(imConvUser);
        }

        return imConvUser;
    }
}
