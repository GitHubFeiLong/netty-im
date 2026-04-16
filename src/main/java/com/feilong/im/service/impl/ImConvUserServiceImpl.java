package com.feilong.im.service.impl;

import com.feilong.im.context.CurrentTimeContext;
import com.feilong.im.entity.ImConvUser;
import com.feilong.im.mapper.ImConvUserMapper;
import com.feilong.im.service.ImConvUserService;
import com.feilong.im.entity.ImConvUser;
import com.feilong.im.dto.ImConvUserDTO;
import com.feilong.im.dto.bo.ImConvUserBO;
import com.feilong.im.dto.vo.ImConvUserVO;
import com.feilong.im.dto.form.ImConvUserForm;
import com.feilong.im.dto.form.ImConvUserSaveForm;
import com.feilong.im.dto.form.ImConvUserUpdateForm;
import com.feilong.im.dto.page.query.ImConvUserPageQuery;
import com.feilong.im.service.ImConvUserService;
import com.feilong.im.mapper.ImConvUserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feilong.im.mapstruct.ImConvUserEntityMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feilong.im.exception.ClientException;
import com.feilong.im.service.ImMessageService;
import com.feilong.im.util.AssertUtil;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

/**
 * im用户会话表 服务接口实现类
 * @author cfl 2026/04/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImConvUserServiceImpl extends ServiceImpl<ImConvUserMapper, ImConvUser> implements ImConvUserService {

    private final ImConvUserMapper imConvUserMapper;
    private final ImMessageService imMessageService;
    private final ImConvUserEntityMapper imConvUserEntityMapper;

    /**
     * im用户会话表分页查询
     *
     * @param pageQuery im用户会话表分页查询参数
     * @return 分页结果
     */
    @Override
    public IPage<ImConvUserVO> page(ImConvUserPageQuery pageQuery) {
        log.debug("分页查询im_conv_user，查询参数：{}", pageQuery);
        // 参数构建
        Page<ImConvUserBO> page = new Page<>(pageQuery.getPage(), pageQuery.getSize());

        // 查询数据
        Page<ImConvUserBO> boPage = imConvUserMapper.page(page, pageQuery);

        // 实体转换
        return imConvUserEntityMapper.toVo(boPage);
    }

    /**
     * 获取im用户会话表表单数据
     *
     * @param id im用户会话表ID
     * @return im用户会话表表单数据
     */
    @Override
    public ImConvUserForm getForm(Long id) {
        log.debug("获取im_conv_user表单数据：{}", id);
        ImConvUser entity = this.getById(id);
        return imConvUserEntityMapper.toForm(entity);
    }

    /**
     * 新增im用户会话表
     *
     * @param formData im用户会话表Save表单对象
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImConvUser save(ImConvUserSaveForm formData) {
        log.debug("新增im_conv_user数据：{}", formData);
        // 实体转换 form->entity
        ImConvUser entity = imConvUserEntityMapper.toEntity(formData);
        boolean flag = this.save(entity);
        if (flag) {
            log.debug("新增im_conv_user数据成功：{}", entity);
            return entity;
        }
        log.warn("新增im_conv_user数据失败");
        throw ClientException.of("保存数据失败，请稍后再试").setServerMessage("保存表im_conv_user失败");
    }

    /**
     * 修改im用户会话表
     *
     * @param id im用户会话表ID
     * @param formData im用户会话表Update表单对象
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImConvUser update(Long id, ImConvUserUpdateForm formData) {
        log.debug("修改im_conv_user，ID：{}，表单数据：{}", id, formData);
        ImConvUser entity = imConvUserEntityMapper.toEntity(formData);

        boolean flag = this.updateById(entity);
        if (flag) {
            log.debug("修改im_conv_user成功：{}", entity);
            return entity;
        }
        log.warn("修改im_conv_user数据失败");
        throw ClientException.of("保存数据失败，请稍后再试").setServerMessage("修改表im_conv_user失败");
    }

    /**
     * 删除im用户会话表
     *
     * @param ids im用户会话表ID，多个以英文逗号(,)分割
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String ids) {
        log.debug("删除im_conv_user数据：{}", ids);
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return this.removeByIds(idList);
    }

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
