package com.feilong.im.mapstruct;

import com.feilong.im.entity.ImConvUser;
import com.feilong.im.dto.ImConvUserDTO;
import com.feilong.im.dto.bo.ImConvUserBO;
import com.feilong.im.dto.vo.ImConvUserVO;
import com.feilong.im.dto.form.ImConvUserForm;
import com.feilong.im.dto.form.ImConvUserSaveForm;
import com.feilong.im.dto.form.ImConvUserUpdateForm;
import com.feilong.im.dto.page.query.ImConvUserPageQuery;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * im用户会话表 EntityMapper
 * @see com.feilong.im.entity.ImConvUser
 * @author cfl 2026/04/16
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImConvUserEntityMapper extends EntityMapper<ImConvUser, ImConvUserDTO> {

    /**
     * 实体转表单
     * @param entity 实体
     * @return 实体对应的表单对象
     */
    ImConvUserForm toForm(ImConvUser entity);

    /**
     * 表单转实体
     * @param form 表单对象
     * @return 表单对应的实体对象
     */
    ImConvUser toEntity(ImConvUserForm form);

    /**
     * Save表单转实体
     * @param saveForm 表单对象
     * @return 表单对应的实体对象
     */
    ImConvUser toEntity(ImConvUserSaveForm saveForm);

    /**
     * Update表单转实体
     * @param updateForm 表单对象
     * @return 表单对应的实体对象
     */
    ImConvUser toEntity(ImConvUserUpdateForm updateForm);

    /**
     * 实体转VO
     * @param entity 实体
     * @return VO
     */
    ImConvUserVO toVo(ImConvUser entity);

    /**
     * 实体集合转VO集合
     * @param entityList 实体集合
     * @return VO集合
     */
    List<ImConvUserVO> toVo(Collection<ImConvUser> entityList);

    /**
     * DTO转Form
     * @param dto 实体DTO
     * @return 表单对象
     */
    ImConvUserForm toForm(ImConvUserDTO dto);

    /**
     * DTO转VO
     * @param dto 实体DTO
     * @return VO
     */
    ImConvUserVO toVo(ImConvUserDTO dto);

    /**
    * 分页结果转换
    * @param bo 源分页信息
    * @return 目标分页信息
    */
    Page<ImConvUserVO> toVo(Page<ImConvUserBO> bo);
}
