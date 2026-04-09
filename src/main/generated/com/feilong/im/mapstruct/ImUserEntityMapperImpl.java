package com.feilong.im.mapstruct;

import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.entity.ImUser;
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
public class ImUserEntityMapperImpl implements ImUserEntityMapper {

    @Override
    public ImUser toEntity(ImUserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ImUser imUser = new ImUser();

        imUser.setCreateTime( dto.getCreateTime() );
        imUser.setUpdateTime( dto.getUpdateTime() );
        imUser.setId( dto.getId() );
        imUser.setNickname( dto.getNickname() );
        imUser.setAvatar( dto.getAvatar() );
        imUser.setStatus( dto.getStatus() );

        return imUser;
    }

    @Override
    public ImUserDTO toDto(ImUser entity) {
        if ( entity == null ) {
            return null;
        }

        ImUserDTO imUserDTO = new ImUserDTO();

        imUserDTO.setId( entity.getId() );
        imUserDTO.setNickname( entity.getNickname() );
        imUserDTO.setAvatar( entity.getAvatar() );
        imUserDTO.setStatus( entity.getStatus() );
        imUserDTO.setCreateTime( entity.getCreateTime() );
        imUserDTO.setUpdateTime( entity.getUpdateTime() );

        return imUserDTO;
    }

    @Override
    public List<ImUser> toEntity(List<ImUserDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<ImUser> list = new ArrayList<ImUser>( dtoList.size() );
        for ( ImUserDTO imUserDTO : dtoList ) {
            list.add( toEntity( imUserDTO ) );
        }

        return list;
    }

    @Override
    public List<ImUserDTO> toDto(List<ImUser> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ImUserDTO> list = new ArrayList<ImUserDTO>( entityList.size() );
        for ( ImUser imUser : entityList ) {
            list.add( toDto( imUser ) );
        }

        return list;
    }
}
