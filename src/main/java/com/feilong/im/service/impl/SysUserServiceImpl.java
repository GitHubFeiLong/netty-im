package com.feilong.im.service.impl;

import com.feilong.im.entity.SysUser;
import com.feilong.im.dto.SysUserDTO;
import com.feilong.im.dto.bo.SysUserBO;
import com.feilong.im.dto.vo.SysUserVO;
import com.feilong.im.dto.form.SysUserForm;
import com.feilong.im.dto.form.SysUserSaveForm;
import com.feilong.im.dto.form.SysUserUpdateForm;
import com.feilong.im.dto.page.query.SysUserPageQuery;
import com.feilong.im.service.SysUserService;
import com.feilong.im.mapper.SysUserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feilong.im.mapstruct.SysUserEntityMapper;
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
 * 系统用户 服务接口实现类
 * @author cfl 2026/04/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserMapper sysUserMapper;
    private final SysUserEntityMapper sysUserEntityMapper;

    /**
     * 系统用户分页查询
     *
     * @param pageQuery 系统用户分页查询参数
     * @return 分页结果
     */
    @Override
    public IPage<SysUserVO> page(SysUserPageQuery pageQuery) {
        log.debug("分页查询sys_user，查询参数：{}", pageQuery);
        // 参数构建
        Page<SysUserBO> page = new Page<>(pageQuery.getPage(), pageQuery.getSize());

        // 查询数据
        Page<SysUserBO> boPage = sysUserMapper.page(page, pageQuery);

        // 实体转换
        return sysUserEntityMapper.toVo(boPage);
    }

    /**
     * 获取系统用户表单数据
     *
     * @param id 系统用户ID
     * @return 系统用户表单数据
     */
    @Override
    public SysUserForm getForm(Long id) {
        log.debug("获取sys_user表单数据：{}", id);
        SysUser entity = this.getById(id);
        return sysUserEntityMapper.toForm(entity);
    }

    /**
     * 新增系统用户
     *
     * @param formData 系统用户Save表单对象
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysUser save(SysUserSaveForm formData) {
        log.debug("新增sys_user数据：{}", formData);
        // 实体转换 form->entity
        SysUser entity = sysUserEntityMapper.toEntity(formData);
        entity.setDeleted(false);
        boolean flag = this.save(entity);
        if (flag) {
            log.debug("新增sys_user数据成功：{}", entity);
            return entity;
        }
        log.warn("新增sys_user数据失败");
        throw ClientException.of("保存数据失败，请稍后再试").setServerMessage("保存表sys_user失败");
    }

    /**
     * 修改系统用户
     *
     * @param id 系统用户ID
     * @param formData 系统用户Update表单对象
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysUser update(Long id, SysUserUpdateForm formData) {
        log.debug("修改sys_user，ID：{}，表单数据：{}", id, formData);
        SysUser entity = sysUserEntityMapper.toEntity(formData);

        boolean flag = this.updateById(entity);
        if (flag) {
            log.debug("修改sys_user成功：{}", entity);
            return entity;
        }
        log.warn("修改sys_user数据失败");
        throw ClientException.of("保存数据失败，请稍后再试").setServerMessage("修改表sys_user失败");
    }

    /**
     * 删除系统用户
     *
     * @param ids 系统用户ID，多个以英文逗号(,)分割
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String ids) {
        log.debug("删除sys_user数据：{}", ids);
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return this.removeByIds(idList);
    }
}
