package com.feilong.im.mapstruct;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feilong.im.entity.ImUser;
import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.dto.form.ImUserForm;
import com.feilong.im.dto.vo.ImUserVO;
import com.feilong.im.dto.bo.ImUserBO;

import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * im账户表 EntityMapper
 * @see com.feilong.im.entity.ImUser
 * @author cfl 2026/04/16
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImUserEntityMapper extends EntityMapper<ImUser, ImUserDTO> {

    /**
     * 实体转表单
     * @param entity 实体
     * @return 实体对应的表单对象
     */
    ImUserForm toForm(ImUser entity);

    /**
     * 表单转实体
     * @param form 表单对象
     * @return 表单对应的实体对象
     */
    ImUser toEntity(ImUserForm form);

    /**
     * 实体转VO
     * @param entity 实体
     * @return VO
     */
    ImUserVO toVo(ImUser entity);

    /**
     * 实体集合转VO集合
     * @param entityList 实体集合
     * @return VO集合
     */
    List<ImUserVO> toVo(Collection<ImUser> entityList);

    /**
     * DTO转Form
     * @param dto 实体DTO
     * @return 表单对象
     */
    ImUserForm toForm(ImUserDTO dto);

    /**
     * DTO转VO
     * @param dto 实体DTO
     * @return VO
     */
    ImUserVO toVo(ImUserDTO dto);

    /**
    * 分页结果转换
    * @param bo 源分页信息
    * @return 目标分页信息
    */
    Page<ImUserVO> toVo(Page<ImUserBO> bo);
}
