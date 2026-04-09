package com.feilong.im.mapstruct;

import com.feilong.im.dto.ImConvUserDTO;
import com.feilong.im.entity.ImConvUser;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-09T19:33:30+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Oracle Corporation)"
)
@Component
public class ImConvUserEntityMapperImpl implements ImConvUserEntityMapper {

    @Override
    public ImConvUser toEntity(ImConvUserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ImConvUser imConvUser = new ImConvUser();

        imConvUser.setId( dto.getId() );
        imConvUser.setConvId( dto.getConvId() );
        imConvUser.setUserId( dto.getUserId() );
        imConvUser.setUnreadCount( dto.getUnreadCount() );
        imConvUser.setLastReadId( dto.getLastReadId() );
        imConvUser.setCreateTime( dto.getCreateTime() );
        imConvUser.setUpdateTime( dto.getUpdateTime() );

        return imConvUser;
    }

    @Override
    public ImConvUserDTO toDto(ImConvUser entity) {
        if ( entity == null ) {
            return null;
        }

        ImConvUserDTO imConvUserDTO = new ImConvUserDTO();

        imConvUserDTO.setId( entity.getId() );
        imConvUserDTO.setConvId( entity.getConvId() );
        imConvUserDTO.setUserId( entity.getUserId() );
        imConvUserDTO.setUnreadCount( entity.getUnreadCount() );
        imConvUserDTO.setLastReadId( entity.getLastReadId() );
        imConvUserDTO.setCreateTime( entity.getCreateTime() );
        imConvUserDTO.setUpdateTime( entity.getUpdateTime() );

        return imConvUserDTO;
    }

    @Override
    public List<ImConvUser> toEntity(List<ImConvUserDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<ImConvUser> list = new ArrayList<ImConvUser>( dtoList.size() );
        for ( ImConvUserDTO imConvUserDTO : dtoList ) {
            list.add( toEntity( imConvUserDTO ) );
        }

        return list;
    }

    @Override
    public List<ImConvUserDTO> toDto(List<ImConvUser> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ImConvUserDTO> list = new ArrayList<ImConvUserDTO>( entityList.size() );
        for ( ImConvUser imConvUser : entityList ) {
            list.add( toDto( imConvUser ) );
        }

        return list;
    }
}
