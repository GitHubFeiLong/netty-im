package com.feilong.im.mapstruct;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feilong.im.entity.ImFriend;
import com.feilong.im.dto.ImFriendDTO;
import com.feilong.im.dto.form.ImFriendForm;
import com.feilong.im.dto.vo.ImFriendVO;
import com.feilong.im.dto.bo.ImFriendBO;

import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 用户好友表 EntityMapper
 * @see com.feilong.im.entity.ImFriend
 * @author cfl 2026/04/16
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImFriendEntityMapper extends EntityMapper<ImFriend, ImFriendDTO> {

    /**
     * 实体转表单
     * @param entity 实体
     * @return 实体对应的表单对象
     */
    ImFriendForm toForm(ImFriend entity);

    /**
     * 表单转实体
     * @param form 表单对象
     * @return 表单对应的实体对象
     */
    ImFriend toEntity(ImFriendForm form);

    /**
     * 实体转VO
     * @param entity 实体
     * @return VO
     */
    ImFriendVO toVo(ImFriend entity);

    /**
     * 实体集合转VO集合
     * @param entityList 实体集合
     * @return VO集合
     */
    List<ImFriendVO> toVo(Collection<ImFriend> entityList);

    /**
     * DTO转Form
     * @param dto 实体DTO
     * @return 表单对象
     */
    ImFriendForm toForm(ImFriendDTO dto);

    /**
     * DTO转VO
     * @param dto 实体DTO
     * @return VO
     */
    ImFriendVO toVo(ImFriendDTO dto);

    /**
    * 分页结果转换
    * @param bo 源分页信息
    * @return 目标分页信息
    */
    Page<ImFriendVO> toVo(Page<ImFriendBO> bo);
}
