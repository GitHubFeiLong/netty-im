package com.feilong.im.mapstruct;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feilong.im.entity.ImConv;
import com.feilong.im.dto.ImConvDTO;
import com.feilong.im.dto.form.ImConvForm;
import com.feilong.im.dto.vo.ImConvVO;
import com.feilong.im.dto.bo.ImConvBO;

import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * im会话表 EntityMapper
 * @see com.feilong.im.entity.ImConv
 * @author cfl 2026/04/16
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImConvEntityMapper extends EntityMapper<ImConv, ImConvDTO> {

    /**
     * 实体转表单
     * @param entity 实体
     * @return 实体对应的表单对象
     */
    ImConvForm toForm(ImConv entity);

    /**
     * 表单转实体
     * @param form 表单对象
     * @return 表单对应的实体对象
     */
    ImConv toEntity(ImConvForm form);

    /**
     * 实体转VO
     * @param entity 实体
     * @return VO
     */
    ImConvVO toVo(ImConv entity);

    /**
     * 实体集合转VO集合
     * @param entityList 实体集合
     * @return VO集合
     */
    List<ImConvVO> toVo(Collection<ImConv> entityList);

    /**
     * DTO转Form
     * @param dto 实体DTO
     * @return 表单对象
     */
    ImConvForm toForm(ImConvDTO dto);

    /**
     * DTO转VO
     * @param dto 实体DTO
     * @return VO
     */
    ImConvVO toVo(ImConvDTO dto);

    /**
    * 分页结果转换
    * @param bo 源分页信息
    * @return 目标分页信息
    */
    Page<ImConvVO> toVo(Page<ImConvBO> bo);
}
