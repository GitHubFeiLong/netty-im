package com.feilong.im.service.impl;

import com.feilong.im.constant.RedisKeyConst;
import com.feilong.im.entity.ImConv;
import com.feilong.im.enums.type.ImConvTypeEnum;
import com.feilong.im.mapper.ImConvMapper;
import com.feilong.im.service.ImConvService;
import com.feilong.im.entity.ImConv;
import com.feilong.im.dto.ImConvDTO;
import com.feilong.im.dto.bo.ImConvBO;
import com.feilong.im.dto.vo.ImConvVO;
import com.feilong.im.dto.form.ImConvForm;
import com.feilong.im.dto.page.query.ImConvPageQuery;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feilong.im.mapstruct.ImConvEntityMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feilong.im.exception.ClientException;
import com.feilong.im.util.AssertUtil;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.feilong.im.util.JsonUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

/**
 * im会话表 服务接口实现类
 * @author cfl 2026/04/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImConvServiceImpl extends ServiceImpl<ImConvMapper, ImConv> implements ImConvService {

    private final ImConvMapper imConvMapper;
    private final ImConvEntityMapper imConvEntityMapper;
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * im会话表分页查询
     *
     * @param pageQuery im会话表分页查询参数
     * @return 分页结果
     */
    @Override
    public IPage<ImConvVO> page(ImConvPageQuery pageQuery) {
        log.debug("分页查询im_conv，查询参数：{}", pageQuery);
        // 参数构建
        Page<ImConvBO> page = new Page<>(pageQuery.getPage(), pageQuery.getSize());

        // 查询数据
        Page<ImConvBO> boPage = imConvMapper.page(page, pageQuery);

        // 实体转换
        return imConvEntityMapper.toVo(boPage);
    }

    /**
     * 获取im会话表表单数据
     *
     * @param id im会话表ID
     * @return im会话表表单数据
     */
    @Override
    public ImConvForm getForm(Long id) {
        log.debug("获取im_conv表单数据：{}", id);
        ImConv entity = this.getById(id);
        return imConvEntityMapper.toForm(entity);
    }

    /**
     * 新增im会话表
     *
     * @param formData im会话表表单对象
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImConv save(ImConvForm formData) {
        log.debug("新增im_conv数据：{}", formData);
        // 实体转换 form->entity
        ImConv entity = imConvEntityMapper.toEntity(formData);
        boolean flag = this.save(entity);
        if (flag) {
            log.debug("新增im_conv数据成功：{}", entity);
            return entity;
        }
        log.warn("新增im_conv数据失败");
        throw ClientException.of("保存数据失败，请稍后再试").setServerMessage("保存表im_conv失败");
    }

    /**
     * 修改im会话表
     *
     * @param id im会话表ID
     * @param formData im会话表表单对象
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImConv update(Long id, ImConvForm formData) {
        log.debug("修改im_conv，ID：{}，表单数据：{}", id, formData);
        ImConv entity = imConvEntityMapper.toEntity(formData);

        boolean flag = this.updateById(entity);
        if (flag) {
            log.debug("修改im_conv成功：{}", entity);
            return entity;
        }
        log.warn("修改im_conv数据失败");
        throw ClientException.of("保存数据失败，请稍后再试").setServerMessage("修改表im_conv失败");
    }

    /**
     * 删除im会话表
     *
     * @param ids im会话表ID，多个以英文逗号(,)分割
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String ids) {
        log.debug("删除im_conv数据：{}", ids);
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return this.removeByIds(idList);
    }

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
