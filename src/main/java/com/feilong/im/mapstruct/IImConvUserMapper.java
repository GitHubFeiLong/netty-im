package com.feilong.im.mapstruct;

import com.feilong.im.dto.ImConvUserDTO;
import com.feilong.im.entity.ImConvUser;
import org.mapstruct.Mapper;

/**
 * <p>
 * im用户会话表
 * </p>
 *
 * @author author
 * @since 2026-03-18
 */
@Mapper(componentModel = "spring")
public interface IImConvUserMapper extends IMapper<ImConvUser, ImConvUserDTO> {

}
