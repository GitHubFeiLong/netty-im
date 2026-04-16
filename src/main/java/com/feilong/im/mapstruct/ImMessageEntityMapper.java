package com.feilong.im.mapstruct;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feilong.im.entity.ImMessage;
import com.feilong.im.dto.ImMessageDTO;
import com.feilong.im.dto.form.ImMessageForm;
import com.feilong.im.dto.vo.ImMessageVO;
import com.feilong.im.dto.bo.ImMessageBO;

import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * im消息表 EntityMapper
 * @see com.feilong.im.entity.ImMessage
 * @author cfl 2026/04/16
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImMessageEntityMapper extends EntityMapper<ImMessage, ImMessageDTO> {

    /**
     * 实体转表单
     * @param entity 实体
     * @return 实体对应的表单对象
     */
    ImMessageForm toForm(ImMessage entity);

    /**
     * 表单转实体
     * @param form 表单对象
     * @return 表单对应的实体对象
     */
    ImMessage toEntity(ImMessageForm form);

    /**
     * 实体转VO
     * @param entity 实体
     * @return VO
     */
    ImMessageVO toVo(ImMessage entity);

    /**
     * 实体集合转VO集合
     * @param entityList 实体集合
     * @return VO集合
     */
    List<ImMessageVO> toVo(Collection<ImMessage> entityList);

    /**
     * DTO转Form
     * @param dto 实体DTO
     * @return 表单对象
     */
    ImMessageForm toForm(ImMessageDTO dto);

    /**
     * DTO转VO
     * @param dto 实体DTO
     * @return VO
     */
    ImMessageVO toVo(ImMessageDTO dto);

    /**
    * 分页结果转换
    * @param bo 源分页信息
    * @return 目标分页信息
    */
    Page<ImMessageVO> toVo(Page<ImMessageBO> bo);
}
