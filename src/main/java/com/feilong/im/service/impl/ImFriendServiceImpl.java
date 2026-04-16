package com.feilong.im.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.entity.ImFriend;
import com.feilong.im.entity.ImUser;
import com.feilong.im.handler.netty.cmd.req.ContactPageReq;
import com.feilong.im.mapper.ImFriendMapper;
import com.feilong.im.mapstruct.ImUserEntityMapper;
import com.feilong.im.service.ImFriendService;
import com.feilong.im.entity.ImFriend;
import com.feilong.im.dto.ImFriendDTO;
import com.feilong.im.dto.bo.ImFriendBO;
import com.feilong.im.dto.vo.ImFriendVO;
import com.feilong.im.dto.form.ImFriendForm;
import com.feilong.im.dto.form.ImFriendSaveForm;
import com.feilong.im.dto.form.ImFriendUpdateForm;
import com.feilong.im.dto.page.query.ImFriendPageQuery;
import com.feilong.im.service.ImFriendService;
import com.feilong.im.mapper.ImFriendMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feilong.im.mapstruct.ImFriendEntityMapper;
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
 * 用户好友表 服务接口实现类
 * @author cfl 2026/04/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImFriendServiceImpl extends ServiceImpl<ImFriendMapper, ImFriend> implements ImFriendService {

    private final ImFriendMapper imFriendMapper;
    private final ImFriendEntityMapper imFriendEntityMapper;
    private final ImUserEntityMapper imUserEntityMapper;

    /**
     * 用户好友表分页查询
     *
     * @param pageQuery 用户好友表分页查询参数
     * @return 分页结果
     */
    @Override
    public IPage<ImFriendVO> page(ImFriendPageQuery pageQuery) {
        log.debug("分页查询im_friend，查询参数：{}", pageQuery);
        // 参数构建
        Page<ImFriendBO> page = new Page<>(pageQuery.getPage(), pageQuery.getSize());

        // 查询数据
        Page<ImFriendBO> boPage = imFriendMapper.page(page, pageQuery);

        // 实体转换
        return imFriendEntityMapper.toVo(boPage);
    }

    /**
     * 获取用户好友表表单数据
     *
     * @param id 用户好友表ID
     * @return 用户好友表表单数据
     */
    @Override
    public ImFriendForm getForm(Long id) {
        log.debug("获取im_friend表单数据：{}", id);
        ImFriend entity = this.getById(id);
        return imFriendEntityMapper.toForm(entity);
    }

    /**
     * 新增用户好友表
     *
     * @param formData 用户好友表Save表单对象
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImFriend save(ImFriendSaveForm formData) {
        log.debug("新增im_friend数据：{}", formData);
        // 实体转换 form->entity
        ImFriend entity = imFriendEntityMapper.toEntity(formData);
        boolean flag = this.save(entity);
        if (flag) {
            log.debug("新增im_friend数据成功：{}", entity);
            return entity;
        }
        log.warn("新增im_friend数据失败");
        throw ClientException.of("保存数据失败，请稍后再试").setServerMessage("保存表im_friend失败");
    }

    /**
     * 修改用户好友表
     *
     * @param id 用户好友表ID
     * @param formData 用户好友表Update表单对象
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImFriend update(Long id, ImFriendUpdateForm formData) {
        log.debug("修改im_friend，ID：{}，表单数据：{}", id, formData);
        ImFriend entity = imFriendEntityMapper.toEntity(formData);

        boolean flag = this.updateById(entity);
        if (flag) {
            log.debug("修改im_friend成功：{}", entity);
            return entity;
        }
        log.warn("修改im_friend数据失败");
        throw ClientException.of("保存数据失败，请稍后再试").setServerMessage("修改表im_friend失败");
    }

    /**
     * 删除用户好友表
     *
     * @param ids 用户好友表ID，多个以英文逗号(,)分割
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String ids) {
        log.debug("删除im_friend数据：{}", ids);
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return this.removeByIds(idList);
    }

    /**
     * 查询用户联系人列表
     *
     * @param imUserId 用户ID
     * @param req  分页参数
     * @return 联系人列表
     */
    @Override
    public Page<ImUserDTO> listPage(Long imUserId, ContactPageReq req) {
        Page page = new Page(req.getPageNum(), req.getPageSize());
        Page<ImUser> imUserPage = imFriendMapper.listPage(page, imUserId, req);
        Page<ImUserDTO> imUserDTOPage = PageDTO.of(imUserPage.getCurrent(), imUserPage.getSize(), imUserPage.getTotal(), true);
        imUserDTOPage.setRecords(imUserEntityMapper.toDto(imUserPage.getRecords()));
        return imUserDTOPage;
    }
}
