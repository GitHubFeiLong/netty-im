package com.feilong.im.mapstruct;

import com.feilong.im.dto.ImConvDTO;
import com.feilong.im.entity.ImConv;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 *
 * @author cfl 2026/3/26
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImConvEntityMapper extends EntityMapper<ImConv, ImConvDTO> {

}
