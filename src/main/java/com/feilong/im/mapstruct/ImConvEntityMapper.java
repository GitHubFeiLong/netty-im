package com.feilong.im.mapstruct;

import com.feilong.im.dto.ImConvDTO;
import com.feilong.im.entity.ImConv;
import org.mapstruct.Mapper;

/**
 *
 * @author cfl 2026/3/26
 */
@Mapper(componentModel = "spring")
public interface ImConvEntityMapper extends EntityMapper<ImConv, ImConvDTO> {

}
