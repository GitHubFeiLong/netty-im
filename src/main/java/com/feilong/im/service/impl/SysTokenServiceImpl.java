package com.feilong.im.service.impl;

import com.feilong.im.config.security.authentication.imuser.ImUserDetails;
import com.feilong.im.config.security.authentication.sysuser.SysUserDetails;
import com.feilong.im.entity.SysToken;
import com.feilong.im.dto.SysTokenDTO;
import com.feilong.im.dto.bo.SysTokenBO;
import com.feilong.im.dto.vo.SysTokenVO;
import com.feilong.im.dto.form.SysTokenForm;
import com.feilong.im.dto.form.SysTokenSaveForm;
import com.feilong.im.dto.form.SysTokenUpdateForm;
import com.feilong.im.dto.page.query.SysTokenPageQuery;
import com.feilong.im.enums.status.SysTokenStatusEnum;
import com.feilong.im.enums.type.SysTokenTypeEnum;
import com.feilong.im.service.SysTokenService;
import com.feilong.im.mapper.SysTokenMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feilong.im.mapstruct.SysTokenEntityMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feilong.im.exception.ClientException;
import com.feilong.im.util.AssertUtil;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

/**
 * 认证用户TOKEN 服务接口实现类
 * @author cfl 2026/04/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysTokenServiceImpl extends ServiceImpl<SysTokenMapper, SysToken> implements SysTokenService {

    private final SysTokenMapper sysTokenMapper;
    private final SysTokenEntityMapper sysTokenEntityMapper;

    /**
     * 认证用户TOKEN分页查询
     *
     * @param pageQuery 认证用户TOKEN分页查询参数
     * @return 分页结果
     */
    @Override
    public IPage<SysTokenVO> page(SysTokenPageQuery pageQuery) {
        log.debug("分页查询sys_token，查询参数：{}", pageQuery);
        // 参数构建
        Page<SysTokenBO> page = new Page<>(pageQuery.getPage(), pageQuery.getSize());

        // 查询数据
        Page<SysTokenBO> boPage = sysTokenMapper.page(page, pageQuery);

        // 实体转换
        return sysTokenEntityMapper.toVo(boPage);
    }

    /**
     * 获取认证用户TOKEN表单数据
     *
     * @param id 认证用户TOKENID
     * @return 认证用户TOKEN表单数据
     */
    @Override
    public SysTokenForm getForm(Long id) {
        log.debug("获取sys_token表单数据：{}", id);
        SysToken entity = this.getById(id);
        return sysTokenEntityMapper.toForm(entity);
    }

    /**
     * 新增认证用户TOKEN
     *
     * @param formData 认证用户TOKENSave表单对象
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysToken save(SysTokenSaveForm formData) {
        log.debug("新增sys_token数据：{}", formData);
        // 实体转换 form->entity
        SysToken entity = sysTokenEntityMapper.toEntity(formData);
        boolean flag = this.save(entity);
        if (flag) {
            log.debug("新增sys_token数据成功：{}", entity);
            return entity;
        }
        log.warn("新增sys_token数据失败");
        throw ClientException.of("保存数据失败，请稍后再试").setServerMessage("保存表sys_token失败");
    }

    /**
     * 修改认证用户TOKEN
     *
     * @param id 认证用户TOKENID
     * @param formData 认证用户TOKENUpdate表单对象
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysToken update(Long id, SysTokenUpdateForm formData) {
        log.debug("修改sys_token，ID：{}，表单数据：{}", id, formData);
        SysToken entity = sysTokenEntityMapper.toEntity(formData);

        boolean flag = this.updateById(entity);
        if (flag) {
            log.debug("修改sys_token成功：{}", entity);
            return entity;
        }
        log.warn("修改sys_token数据失败");
        throw ClientException.of("保存数据失败，请稍后再试").setServerMessage("修改表sys_token失败");
    }

    /**
     * 删除认证用户TOKEN
     *
     * @param ids 认证用户TOKENID，多个以英文逗号(,)分割
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String ids) {
        log.debug("删除sys_token数据：{}", ids);
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return this.removeByIds(idList);
    }

    /**
     * 保存认证用户TOKEN
     *
     * @param accessToken    访问TOKEN
     * @param authentication 认证信息
     * @return 认证用户TOKEN
     */
    @Override
    public SysToken save(String accessToken, Authentication authentication) {
        SysTokenTypeEnum type;
        String tokenId;
        Long userId;
        Object principal = authentication.getPrincipal();
        if (principal instanceof ImUserDetails userDetails) {
            type = SysTokenTypeEnum.IM_USER;
            tokenId = userDetails.getTokenId();
            userId = userDetails.getId();
        } else {
            SysUserDetails userDetails = (SysUserDetails) principal;
            type = SysTokenTypeEnum.SYS_USER;
            tokenId = userDetails.getTokenId();
            userId = userDetails.getId();
        }
        SysToken sysToken = new SysToken();
        sysToken.setId(tokenId);
        sysToken.setToken(accessToken);
        sysToken.setType(type.getId());
        sysToken.setUserId(userId);
        sysToken.setStatus(SysTokenStatusEnum.AVAILABLE.getId());
        sysToken.setRemark("认证成功颁发令牌或刷新令牌");

        super.save(sysToken);

        return sysToken;
    }
}
