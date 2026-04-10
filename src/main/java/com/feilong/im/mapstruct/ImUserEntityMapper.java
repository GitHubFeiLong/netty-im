package com.feilong.im.mapstruct;

import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.entity.ImUser;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>
 * im账户表
 * </p>
 *
 * @author author
 * @since 2026-03-18
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImUserEntityMapper extends EntityMapper<ImUser, ImUserDTO> {

}
