package com.feilong.im.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feilong.im.constant.RedisKeyConst;
import com.feilong.im.context.CurrentTimeContext;
import com.feilong.im.dao.ImConvMapper;
import com.feilong.im.dto.ImConvDTO;
import com.feilong.im.entity.ImConv;
import com.feilong.im.entity.ImConvUser;
import com.feilong.im.enums.type.ImConvTypeEnum;
import com.feilong.im.mapstruct.ImConvEntityMapper;
import com.feilong.im.service.ImConvService;
import com.feilong.im.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * im会话表 服务实现类
 * </p>
 *
 * @author author
 * @since 2026-02-25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImConvServiceImpl extends ServiceImpl<ImConvMapper, ImConv> implements ImConvService {


    private final ImConvEntityMapper imConvEntityMapper;
    private final StringRedisTemplate stringRedisTemplate;
    // private final ImUserMapper imUserMapper;
    // private final ImGroupMapper imGroupMapper;
    // private final ImConvMapper imConvMapper;
    // private final ImUserEntityMapper imUserEntityMapper;
    // private final ImGroupEntityMapper imGroupEntityMapper;

    /**
     * 获取缓存中的用户信息
     * @param minUserId 较小的用户ID
     * @param maxUserId 较大的用户ID
     * @return 用户信息
     */
    @Override
    public ImConvDTO getSingleCatchById(Long minUserId, Long maxUserId) {

        String key = RedisKeyConst.getKey(RedisKeyConst.IM_CONV_KEY, ImConvTypeEnum.SINGLE.getId(), minUserId, maxUserId);

        if (stringRedisTemplate.hasKey(key)) {
            String str = stringRedisTemplate.opsForValue().get(key);
            return JsonUtil.toObject(str, ImConvDTO.class);
        }
        synchronized (this) {
            if (stringRedisTemplate.hasKey(key)) {
                String str = stringRedisTemplate.opsForValue().get(key);
                return JsonUtil.toObject(str, ImConvDTO.class);
            }

            ImConv imConv = super.lambdaQuery()
                    .eq(ImConv::getType, ImConvTypeEnum.SINGLE.getId())
                    .eq(ImConv::getUser1Id, minUserId)
                    .eq(ImConv::getUser2Id, maxUserId)
                    .one();

            ImConvDTO dto = imConvEntityMapper.toDto(imConv);
            stringRedisTemplate.opsForValue().set(key, JsonUtil.toJsonString(dto), 1, TimeUnit.DAYS);

            return dto;
        }
    }


    // /**
    //  * 根据会话ID查询会话信息
    //  *
    //  * @param convId 会话ID
    //  * @return 会话信息
    //  */
    // @Override
    // public ImConvDTO getCatchById(Long convId) {
    //     String key = RedisKeyConst.getKey(RedisKeyConst.IM_CONV_KEY, convId);
    //
    //     if (stringRedisTemplate.hasKey(key)) {
    //         ImConvDTO dto = JsonUtil.toObject(stringRedisTemplate.opsForValue().get(key), ImConvDTO.class);
    //         // 设置用户状态
    //         if (dto.getUser1() != null) {
    //             // 查询实时状态
    //             boolean userOnline = NettyServerHandler.isUserOnline(dto.getUser1().getId());
    //             dto.getUser1().setStatus(userOnline ? ImUserStatusEnum.ONLINE.getId() : ImUserStatusEnum.OFFLINE.getId());
    //         }
    //         if (dto.getUser2() != null) {
    //             // 查询实时状态
    //             boolean userOnline = NettyServerHandler.isUserOnline(dto.getUser2().getId());
    //             dto.getUser2().setStatus(userOnline ? ImUserStatusEnum.ONLINE.getId() : ImUserStatusEnum.OFFLINE.getId());
    //         }
    //         return dto;
    //     }
    //
    //     synchronized (this) {
    //         if (stringRedisTemplate.hasKey(key)) {
    //             ImConvDTO dto = JsonUtil.toObject(stringRedisTemplate.opsForValue().get(key), ImConvDTO.class);
    //             // 设置用户状态
    //             if (dto.getUser1() != null) {
    //                 // 查询实时状态
    //                 boolean userOnline = NettyServerHandler.isUserOnline(dto.getUser1().getId());
    //                 dto.getUser1().setStatus(userOnline ? ImUserStatusEnum.ONLINE.getId() : ImUserStatusEnum.OFFLINE.getId());
    //             }
    //             if (dto.getUser2() != null) {
    //                 // 查询实时状态
    //                 boolean userOnline = NettyServerHandler.isUserOnline(dto.getUser2().getId());
    //                 dto.getUser2().setStatus(userOnline ? ImUserStatusEnum.ONLINE.getId() : ImUserStatusEnum.OFFLINE.getId());
    //             }
    //
    //             return dto;
    //         }
    //
    //         ImConvDTO dto = imConvEntityMapper.toDto(super.getById(convId));
    //         // 填充信息
    //         ImUser user1 = imUserMapper.selectById(dto.getUser1Id());
    //         dto.setUser1(imUserEntityMapper.toDto(user1));
    //         if (Objects.equals(dto.getConvType(), ImConvTypeEnum.SINGLE.getId())) {
    //             ImUser user2 = imUserMapper.selectById(dto.getUser2Id());
    //             dto.setUser2(imUserEntityMapper.toDto(user2));
    //         } else {
    //             ImGroup imGroup = imGroupMapper.selectById(dto.getGroupId());
    //             dto.setGroup(imGroupEntityMapper.toDto(imGroup));
    //         }
    //
    //         // 设置用户状态
    //         if (dto.getUser1() != null) {
    //             // 查询实时状态
    //             boolean userOnline = NettyServerHandler.isUserOnline(dto.getUser1().getId());
    //             dto.getUser1().setStatus(userOnline ? ImUserStatusEnum.ONLINE.getId() : ImUserStatusEnum.OFFLINE.getId());
    //         }
    //         if (dto.getUser2() != null) {
    //             // 查询实时状态
    //             boolean userOnline = NettyServerHandler.isUserOnline(dto.getUser2().getId());
    //             dto.getUser2().setStatus(userOnline ? ImUserStatusEnum.ONLINE.getId() : ImUserStatusEnum.OFFLINE.getId());
    //         }
    //
    //         stringRedisTemplate.opsForValue().set(key, JsonUtil.toJsonString(dto));
    //         return dto;
    //     }
    // }
    //
    // /**
    //  * 更新会话最后一条消息ID
    //  *
    //  * @param convId    会话ID
    //  * @param lastMsgId 消息ID
    //  * @return 更新结果
    //  */
    // @Override
    // @Transactional(rollbackFor = Exception.class)
    // public boolean updateLastMsgId(Long convId, Long lastMsgId) {
    //     log.debug("更新会话{}的最后一条消息ID为{}", convId, lastMsgId);
    //     return super.lambdaUpdate()
    //             .set(ImConv::getLastMsgId, lastMsgId)
    //             .set(ImConv::getUpdateTime, CurrentTimeContext.get())
    //             .eq(ImConv::getId, convId)
    //             .update();
    // }
    //
    // /**
    //  * 查询用户相关的指定类型的所有会话ID
    //  *
    //  * @param userId   用户ID
    //  * @param convType 会话类型
    //  * @return 会话ID集合
    //  */
    // @Override
    // public Set<Long> queryConvIdsByUserId(Long userId, ImConvTypeEnum convType) {
    //     return imConvMapper.queryConvIdsByUserId(userId, convType != null ? convType.getId() : null);
    // }
}
