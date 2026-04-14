package com.feilong.im.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feilong.im.config.NettyServerHandler;
import com.feilong.im.constant.RedisKeyConst;
import com.feilong.im.context.CurrentTimeContext;
import com.feilong.im.dao.ImUserMapper;
import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.dto.req.ImSignUpReq;
import com.feilong.im.entity.ImUser;
import com.feilong.im.enums.status.ImUserStatusEnum;
import com.feilong.im.mapstruct.ImUserEntityMapper;
import com.feilong.im.service.ImUserService;
import com.feilong.im.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * im账户表 服务实现类
 * </p>
 *
 * @author author
 * @since 2026-02-25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImUserServiceImpl extends ServiceImpl<ImUserMapper, ImUser> implements ImUserService {

    private final ImUserMapper imUserMapper;
    private final StringRedisTemplate StringRedisTemplate;
    private final ImUserEntityMapper imUserEntityMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * 获取缓存中的用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    @Override
    public ImUserDTO getCatchById(Long userId) {
        String key = RedisKeyConst.getKey(RedisKeyConst.IM_USER_KEY, userId);
        if (StringRedisTemplate.hasKey(key)) {
            ImUserDTO imUserDTO = JsonUtil.toObject(StringRedisTemplate.opsForValue().get(key), ImUserDTO.class);
            // 查询实时状态
            boolean userOnline = NettyServerHandler.isUserOnline(userId);
            imUserDTO.setStatus(userOnline ? ImUserStatusEnum.ONLINE.getId() : ImUserStatusEnum.OFFLINE.getId());
            return imUserDTO;
        }

        synchronized (this) {
            if (StringRedisTemplate.hasKey(key)) {
                ImUserDTO imUserDTO = JsonUtil.toObject(StringRedisTemplate.opsForValue().get(key), ImUserDTO.class);
                // 查询实时状态
                boolean userOnline = NettyServerHandler.isUserOnline(userId);
                imUserDTO.setStatus(userOnline ? ImUserStatusEnum.ONLINE.getId() : ImUserStatusEnum.OFFLINE.getId());
                return imUserDTO;
            }

            ImUserDTO dto = imUserEntityMapper.toDto(super.getById(userId));

            StringRedisTemplate.opsForValue().set(key, JsonUtil.toJsonString(dto));
            return dto;
        }
    }

    /**
     * 根据用户ID查询，不存在时进行创建
     *
     * @param userId   用户ID
     * @return IM账户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImUser getOrCreateById(Long userId) {
        ImUser imUser = super.getById(userId);
        if (imUser != null) {
            return imUser;
        }

        synchronized (this) {
            // 二查
            imUser = super.getById(userId);
            if (imUser != null) {
                return imUser;
            }
        }

        throw new RuntimeException("创建IM账户" + userId + "失败");
    }


    /**
     * 更新用户状态
     *
     * @param id 用户ID
     * @param status 用户状态（0-离线，1-在线，2-忙碌，3-隐身）
     * @return 更新结果
     */
    @Override
    public boolean updateStatus(Long id, int status) {
        return super.lambdaUpdate().set(ImUser::getStatus, status).set(ImUser::getUpdateTime, CurrentTimeContext.get()).eq(ImUser::getId, id).update();
    }

    /**
     * 根据用户ID列表查询用户信息
     *
     * @param userIds 用户ID列表
     * @return 用户信息列表
     */
    @Override
    public List<ImUser> listInfoByUserIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyList();
        }
        return lambdaQuery().select(ImUser::getId, ImUser::getNickname, ImUser::getAvatar, ImUser::getStatus).in(ImUser::getId, userIds).list();
    }
}
