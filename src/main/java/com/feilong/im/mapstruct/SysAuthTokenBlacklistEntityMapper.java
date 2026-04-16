package com.feilong.im.mapstruct;

import com.feilong.im.entity.SysAuthTokenBlacklist;
import com.feilong.im.dto.SysAuthTokenBlacklistDTO;
import com.feilong.im.dto.bo.SysAuthTokenBlacklistBO;
import com.feilong.im.dto.vo.SysAuthTokenBlacklistVO;
import com.feilong.im.dto.form.SysAuthTokenBlacklistForm;
import com.feilong.im.dto.form.SysAuthTokenBlacklistSaveForm;
import com.feilong.im.dto.form.SysAuthTokenBlacklistUpdateForm;
import com.feilong.im.dto.page.query.SysAuthTokenBlacklistPageQuery;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 认证token 黑名单 EntityMapper
 * @see com.feilong.im.entity.SysAuthTokenBlacklist
 * @author cfl 2026/04/16
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysAuthTokenBlacklistEntityMapper extends EntityMapper<SysAuthTokenBlacklist, SysAuthTokenBlacklistDTO> {

    /**
     * 实体转表单
     * @param entity 实体
     * @return 实体对应的表单对象
     */
    SysAuthTokenBlacklistForm toForm(SysAuthTokenBlacklist entity);

    /**
     * 表单转实体
     * @param form 表单对象
     * @return 表单对应的实体对象
     */
    SysAuthTokenBlacklist toEntity(SysAuthTokenBlacklistForm form);

    /**
     * Save表单转实体
     * @param saveForm 表单对象
     * @return 表单对应的实体对象
     */
    SysAuthTokenBlacklist toEntity(SysAuthTokenBlacklistSaveForm saveForm);

    /**
     * Update表单转实体
     * @param updateForm 表单对象
     * @return 表单对应的实体对象
     */
    SysAuthTokenBlacklist toEntity(SysAuthTokenBlacklistUpdateForm updateForm);

    /**
     * 实体转VO
     * @param entity 实体
     * @return VO
     */
    SysAuthTokenBlacklistVO toVo(SysAuthTokenBlacklist entity);

    /**
     * 实体集合转VO集合
     * @param entityList 实体集合
     * @return VO集合
     */
    List<SysAuthTokenBlacklistVO> toVo(Collection<SysAuthTokenBlacklist> entityList);

    /**
     * DTO转Form
     * @param dto 实体DTO
     * @return 表单对象
     */
    SysAuthTokenBlacklistForm toForm(SysAuthTokenBlacklistDTO dto);

    /**
     * DTO转VO
     * @param dto 实体DTO
     * @return VO
     */
    SysAuthTokenBlacklistVO toVo(SysAuthTokenBlacklistDTO dto);

    /**
    * 分页结果转换
    * @param bo 源分页信息
    * @return 目标分页信息
    */
    Page<SysAuthTokenBlacklistVO> toVo(Page<SysAuthTokenBlacklistBO> bo);
}
