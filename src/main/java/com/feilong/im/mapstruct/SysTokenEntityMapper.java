package com.feilong.im.mapstruct;

import com.feilong.im.entity.SysToken;
import com.feilong.im.dto.SysTokenDTO;
import com.feilong.im.dto.bo.SysTokenBO;
import com.feilong.im.dto.vo.SysTokenVO;
import com.feilong.im.dto.form.SysTokenForm;
import com.feilong.im.dto.form.SysTokenSaveForm;
import com.feilong.im.dto.form.SysTokenUpdateForm;
import com.feilong.im.dto.page.query.SysTokenPageQuery;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 认证用户TOKEN EntityMapper
 * @see com.feilong.im.entity.SysToken
 * @author cfl 2026/04/23
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysTokenEntityMapper extends EntityMapper<SysToken, SysTokenDTO> {

    /**
     * 实体转表单
     * @param entity 实体
     * @return 实体对应的表单对象
     */
    SysTokenForm toForm(SysToken entity);

    /**
     * 表单转实体
     * @param form 表单对象
     * @return 表单对应的实体对象
     */
    SysToken toEntity(SysTokenForm form);

    /**
     * Save表单转实体
     * @param saveForm 表单对象
     * @return 表单对应的实体对象
     */
    SysToken toEntity(SysTokenSaveForm saveForm);

    /**
     * Update表单转实体
     * @param updateForm 表单对象
     * @return 表单对应的实体对象
     */
    SysToken toEntity(SysTokenUpdateForm updateForm);

    /**
     * 实体转VO
     * @param entity 实体
     * @return VO
     */
    SysTokenVO toVo(SysToken entity);

    /**
     * 实体集合转VO集合
     * @param entityList 实体集合
     * @return VO集合
     */
    List<SysTokenVO> toVo(Collection<SysToken> entityList);

    /**
     * DTO转Form
     * @param dto 实体DTO
     * @return 表单对象
     */
    SysTokenForm toForm(SysTokenDTO dto);

    /**
     * DTO转VO
     * @param dto 实体DTO
     * @return VO
     */
    SysTokenVO toVo(SysTokenDTO dto);

    /**
    * 分页结果转换
    * @param bo 源分页信息
    * @return 目标分页信息
    */
    Page<SysTokenVO> toVo(Page<SysTokenBO> bo);
}
