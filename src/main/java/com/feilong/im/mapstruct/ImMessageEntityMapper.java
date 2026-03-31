package com.feilong.im.mapstruct;

import com.feilong.im.dto.ImMessageDTO;
import com.feilong.im.entity.ImMessage;
import org.mapstruct.Mapper;

/**
 * <p>
 * im消息表
 * </p>
 *
 * @author author
 * @since 2026-03-18
 */
@Mapper(componentModel = "spring")
public interface ImMessageEntityMapper extends EntityMapper<ImMessage, ImMessageDTO> {

}
