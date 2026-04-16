package com.feilong.im.service.impl;

import com.feilong.im.entity.ImMessage;
import com.feilong.im.enums.status.ImMessageStatusEnum;
import com.feilong.im.mapper.ImMessageMapper;
import com.feilong.im.service.ImMessageService;
import com.feilong.im.entity.ImMessage;
import com.feilong.im.dto.ImMessageDTO;
import com.feilong.im.dto.bo.ImMessageBO;
import com.feilong.im.dto.vo.ImMessageVO;
import com.feilong.im.dto.form.ImMessageForm;
import com.feilong.im.dto.page.query.ImMessagePageQuery;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feilong.im.mapstruct.ImMessageEntityMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feilong.im.exception.ClientException;
import com.feilong.im.util.AssertUtil;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

/**
 * im消息表 服务接口实现类
 * @author cfl 2026/04/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImMessageServiceImpl extends ServiceImpl<ImMessageMapper, ImMessage> implements ImMessageService {

    private final ImMessageMapper imMessageMapper;
    private final ImMessageEntityMapper imMessageEntityMapper;

    /**
     * im消息表分页查询
     *
     * @param pageQuery im消息表分页查询参数
     * @return 分页结果
     */
    @Override
    public IPage<ImMessageVO> page(ImMessagePageQuery pageQuery) {
        log.debug("分页查询im_message，查询参数：{}", pageQuery);
        // 参数构建
        Page<ImMessageBO> page = new Page<>(pageQuery.getPage(), pageQuery.getSize());

        // 查询数据
        Page<ImMessageBO> boPage = imMessageMapper.page(page, pageQuery);

        // 实体转换
        return imMessageEntityMapper.toVo(boPage);
    }

    /**
     * 获取im消息表表单数据
     *
     * @param id im消息表ID
     * @return im消息表表单数据
     */
    @Override
    public ImMessageForm getForm(Long id) {
        log.debug("获取im_message表单数据：{}", id);
        ImMessage entity = this.getById(id);
        return imMessageEntityMapper.toForm(entity);
    }

    /**
     * 新增im消息表
     *
     * @param formData im消息表表单对象
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImMessage save(ImMessageForm formData) {
        log.debug("新增im_message数据：{}", formData);
        // 实体转换 form->entity
        ImMessage entity = imMessageEntityMapper.toEntity(formData);
        boolean flag = this.save(entity);
        if (flag) {
            log.debug("新增im_message数据成功：{}", entity);
            return entity;
        }
        log.warn("新增im_message数据失败");
        throw ClientException.of("保存数据失败，请稍后再试").setServerMessage("保存表im_message失败");
    }

    /**
     * 修改im消息表
     *
     * @param id im消息表ID
     * @param formData im消息表表单对象
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImMessage update(Long id, ImMessageForm formData) {
        log.debug("修改im_message，ID：{}，表单数据：{}", id, formData);
        ImMessage entity = imMessageEntityMapper.toEntity(formData);

        boolean flag = this.updateById(entity);
        if (flag) {
            log.debug("修改im_message成功：{}", entity);
            return entity;
        }
        log.warn("修改im_message数据失败");
        throw ClientException.of("保存数据失败，请稍后再试").setServerMessage("修改表im_message失败");
    }

    /**
     * 删除im消息表
     *
     * @param ids im消息表ID，多个以英文逗号(,)分割
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String ids) {
        log.debug("删除im_message数据：{}", ids);
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return this.removeByIds(idList);
    }

    /**
     * 获取未读消息数量
     *
     * @param convId     会话ID
     * @param receiverId 接收者ID
     * @return 未读消息数量
     */
    @Override
    public int countUnreadMessage(Long convId, Long receiverId) {
        return super.lambdaQuery()
                .eq(ImMessage::getConvId, convId)
                .eq(ImMessage::getReceiverId, receiverId)
                .between(ImMessage::getStatus, ImMessageStatusEnum.DELIVERED_TO_SERVER.getId(), ImMessageStatusEnum.DELIVERED_TO_RECEIVER.getId())
                .count()
                .intValue();
    }
}
