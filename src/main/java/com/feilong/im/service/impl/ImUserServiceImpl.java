package com.feilong.im.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feilong.im.constant.RedisKeyConst;
import com.feilong.im.context.CurrentTimeContext;
import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.dto.bo.ImUserBO;
import com.feilong.im.dto.vo.ImUserVO;
import com.feilong.im.dto.form.ImUserForm;
import com.feilong.im.dto.form.ImUserSaveForm;
import com.feilong.im.dto.form.ImUserUpdateForm;
import com.feilong.im.dto.page.query.ImUserPageQuery;
import com.feilong.im.dto.vo.ImUserVO;
import com.feilong.im.entity.ImUser;
import com.feilong.im.enums.status.ImUserStatusEnum;
import com.feilong.im.exception.ClientException;
import com.feilong.im.handler.netty.NettyServerHandler;
import com.feilong.im.mapper.ImUserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feilong.im.mapstruct.ImUserEntityMapper;
import com.feilong.im.service.ImUserService;
import com.feilong.im.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

/**
 * im账户表 服务接口实现类
 * @author cfl 2026/04/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImUserServiceImpl extends ServiceImpl<ImUserMapper, ImUser> implements ImUserService {

    private final ImUserMapper imUserMapper;
    private final ImUserEntityMapper imUserEntityMapper;
    private final StringRedisTemplate StringRedisTemplate;
    private final PasswordEncoder passwordEncoder;

    /**
     * im账户表分页查询
     *
     * @param pageQuery im账户表分页查询参数
     * @return 分页结果
     */
    @Override
    public IPage<ImUserVO> page(ImUserPageQuery pageQuery) {
        log.debug("分页查询im_user，查询参数：{}", pageQuery);
        // 参数构建
        Page<ImUserBO> page = new Page<>(pageQuery.getPage(), pageQuery.getSize());

        // 查询数据
        Page<ImUserBO> boPage = imUserMapper.page(page, pageQuery);

        // 实体转换
        return imUserEntityMapper.toVo(boPage);
    }

    /**
     * 获取im账户表表单数据
     *
     * @param id im账户表ID
     * @return im账户表表单数据
     */
    @Override
    public ImUserForm getForm(Long id) {
        log.debug("获取im_user表单数据：{}", id);
        ImUser entity = this.getById(id);
        return imUserEntityMapper.toForm(entity);
    }

    /**
     * 新增im账户表
     *
     * @param formData im账户表Save表单对象
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImUser save(ImUserSaveForm formData) {
        log.debug("新增im_user数据：{}", formData);
        // 实体转换 form->entity
        ImUser entity = imUserEntityMapper.toEntity(formData);
        entity.setDeleted(0L);
        boolean flag = this.save(entity);
        if (flag) {
            log.debug("新增im_user数据成功：{}", entity);
            return entity;
        }
        log.warn("新增im_user数据失败");
        throw ClientException.of("保存数据失败，请稍后再试").setServerMessage("保存表im_user失败");
    }

    /**
     * 修改im账户表
     *
     * @param id im账户表ID
     * @param formData im账户表Update表单对象
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImUser update(Long id, ImUserUpdateForm formData) {
        log.debug("修改im_user，ID：{}，表单数据：{}", id, formData);
        ImUser entity = imUserEntityMapper.toEntity(formData);

        boolean flag = this.updateById(entity);
        if (flag) {
            log.debug("修改im_user成功：{}", entity);
            return entity;
        }
        log.warn("修改im_user数据失败");
        throw ClientException.of("保存数据失败，请稍后再试").setServerMessage("修改表im_user失败");
    }

    /**
     * 删除im账户表
     *
     * @param ids im账户表ID，多个以英文逗号(,)分割
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String ids) {
        log.debug("删除im_user数据：{}", ids);
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return this.removeByIds(idList);
    }

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
