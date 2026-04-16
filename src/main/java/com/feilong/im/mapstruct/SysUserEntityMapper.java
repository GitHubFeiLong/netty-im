package com.feilong.im.mapstruct;

import com.feilong.im.entity.SysUser;
import com.feilong.im.dto.SysUserDTO;
import com.feilong.im.dto.bo.SysUserBO;
import com.feilong.im.dto.vo.SysUserVO;
import com.feilong.im.dto.form.SysUserForm;
import com.feilong.im.dto.form.SysUserSaveForm;
import com.feilong.im.dto.form.SysUserUpdateForm;
import com.feilong.im.dto.page.query.SysUserPageQuery;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 系统用户 EntityMapper
 * @see com.feilong.im.entity.SysUser
 * @author cfl 2026/04/16
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysUserEntityMapper extends EntityMapper<SysUser, SysUserDTO> {

    /**
     * 实体转表单
     * @param entity 实体
     * @return 实体对应的表单对象
     */
    SysUserForm toForm(SysUser entity);

    /**
     * 表单转实体
     * @param form 表单对象
     * @return 表单对应的实体对象
     */
    SysUser toEntity(SysUserForm form);

    /**
     * Save表单转实体
     * @param saveForm 表单对象
     * @return 表单对应的实体对象
     */
    SysUser toEntity(SysUserSaveForm saveForm);

    /**
     * Update表单转实体
     * @param updateForm 表单对象
     * @return 表单对应的实体对象
     */
    SysUser toEntity(SysUserUpdateForm updateForm);

    /**
     * 实体转VO
     * @param entity 实体
     * @return VO
     */
    SysUserVO toVo(SysUser entity);

    /**
     * 实体集合转VO集合
     * @param entityList 实体集合
     * @return VO集合
     */
    List<SysUserVO> toVo(Collection<SysUser> entityList);

    /**
     * DTO转Form
     * @param dto 实体DTO
     * @return 表单对象
     */
    SysUserForm toForm(SysUserDTO dto);

    /**
     * DTO转VO
     * @param dto 实体DTO
     * @return VO
     */
    SysUserVO toVo(SysUserDTO dto);

    /**
    * 分页结果转换
    * @param bo 源分页信息
    * @return 目标分页信息
    */
    Page<SysUserVO> toVo(Page<SysUserBO> bo);
}
