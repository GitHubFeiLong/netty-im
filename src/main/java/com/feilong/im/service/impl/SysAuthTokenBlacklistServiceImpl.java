package com.feilong.im.service.impl;

import com.feilong.im.entity.SysAuthTokenBlacklist;
import com.feilong.im.dto.SysAuthTokenBlacklistDTO;
import com.feilong.im.dto.bo.SysAuthTokenBlacklistBO;
import com.feilong.im.dto.vo.SysAuthTokenBlacklistVO;
import com.feilong.im.dto.form.SysAuthTokenBlacklistForm;
import com.feilong.im.dto.form.SysAuthTokenBlacklistSaveForm;
import com.feilong.im.dto.form.SysAuthTokenBlacklistUpdateForm;
import com.feilong.im.dto.page.query.SysAuthTokenBlacklistPageQuery;
import com.feilong.im.service.SysAuthTokenBlacklistService;
import com.feilong.im.mapper.SysAuthTokenBlacklistMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feilong.im.mapstruct.SysAuthTokenBlacklistEntityMapper;
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
 * 认证token 黑名单 服务接口实现类
 * @author cfl 2026/04/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysAuthTokenBlacklistServiceImpl extends ServiceImpl<SysAuthTokenBlacklistMapper, SysAuthTokenBlacklist> implements SysAuthTokenBlacklistService {

    private final SysAuthTokenBlacklistMapper sysAuthTokenBlacklistMapper;
    private final SysAuthTokenBlacklistEntityMapper sysAuthTokenBlacklistEntityMapper;

    /**
     * 认证token 黑名单分页查询
     *
     * @param pageQuery 认证token 黑名单分页查询参数
     * @return 分页结果
     */
    @Override
    public IPage<SysAuthTokenBlacklistVO> page(SysAuthTokenBlacklistPageQuery pageQuery) {
        log.debug("分页查询sys_auth_token_blacklist，查询参数：{}", pageQuery);
        // 参数构建
        Page<SysAuthTokenBlacklistBO> page = new Page<>(pageQuery.getPage(), pageQuery.getSize());

        // 查询数据
        Page<SysAuthTokenBlacklistBO> boPage = sysAuthTokenBlacklistMapper.page(page, pageQuery);

        // 实体转换
        return sysAuthTokenBlacklistEntityMapper.toVo(boPage);
    }

    /**
     * 获取认证token 黑名单表单数据
     *
     * @param id 认证token 黑名单ID
     * @return 认证token 黑名单表单数据
     */
    @Override
    public SysAuthTokenBlacklistForm getForm(Long id) {
        log.debug("获取sys_auth_token_blacklist表单数据：{}", id);
        SysAuthTokenBlacklist entity = this.getById(id);
        return sysAuthTokenBlacklistEntityMapper.toForm(entity);
    }

    /**
     * 新增认证token 黑名单
     *
     * @param formData 认证token 黑名单Save表单对象
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysAuthTokenBlacklist save(SysAuthTokenBlacklistSaveForm formData) {
        log.debug("新增sys_auth_token_blacklist数据：{}", formData);
        // 实体转换 form->entity
        SysAuthTokenBlacklist entity = sysAuthTokenBlacklistEntityMapper.toEntity(formData);
        entity.setDeleted(0L);
        boolean flag = this.save(entity);
        if (flag) {
            log.debug("新增sys_auth_token_blacklist数据成功：{}", entity);
            return entity;
        }
        log.warn("新增sys_auth_token_blacklist数据失败");
        throw ClientException.of("保存数据失败，请稍后再试").setServerMessage("保存表sys_auth_token_blacklist失败");
    }

    /**
     * 修改认证token 黑名单
     *
     * @param id 认证token 黑名单ID
     * @param formData 认证token 黑名单Update表单对象
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysAuthTokenBlacklist update(Long id, SysAuthTokenBlacklistUpdateForm formData) {
        log.debug("修改sys_auth_token_blacklist，ID：{}，表单数据：{}", id, formData);
        SysAuthTokenBlacklist entity = sysAuthTokenBlacklistEntityMapper.toEntity(formData);

        boolean flag = this.updateById(entity);
        if (flag) {
            log.debug("修改sys_auth_token_blacklist成功：{}", entity);
            return entity;
        }
        log.warn("修改sys_auth_token_blacklist数据失败");
        throw ClientException.of("保存数据失败，请稍后再试").setServerMessage("修改表sys_auth_token_blacklist失败");
    }

    /**
     * 删除认证token 黑名单
     *
     * @param ids 认证token 黑名单ID，多个以英文逗号(,)分割
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String ids) {
        log.debug("删除sys_auth_token_blacklist数据：{}", ids);
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return this.removeByIds(idList);
    }

    /**
     * 保存黑名单token ID，如果存在不进行操作，如果不存在就保存
     *
     * @param id token ID
     * @param token token
     * @return 保存结果
     */
    @Override
    public boolean save(String id, String token) {
        SysAuthTokenBlacklist sysAuthTokenBlacklist = super.getById(id);
        if (sysAuthTokenBlacklist != null) {
            return false;
        }

        synchronized (this) {
            sysAuthTokenBlacklist = super.getById(id);
            if (sysAuthTokenBlacklist != null) {
                return false;
            }

            sysAuthTokenBlacklist = new SysAuthTokenBlacklist();
            sysAuthTokenBlacklist.setDeleted(0L);
            sysAuthTokenBlacklist.setId(id);
            sysAuthTokenBlacklist.setToken(token);
            return super.save(sysAuthTokenBlacklist);
        }
    }
}
