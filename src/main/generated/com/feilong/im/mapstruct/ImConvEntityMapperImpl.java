package com.feilong.im.mapstruct;

import com.feilong.im.dto.ImConvDTO;
import com.feilong.im.entity.ImConv;
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
public class ImConvEntityMapperImpl implements ImConvEntityMapper {

    @Override
    public ImConv toEntity(ImConvDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ImConv imConv = new ImConv();

        imConv.setId( dto.getId() );
        imConv.setUser1Id( dto.getUser1Id() );
        imConv.setUser2Id( dto.getUser2Id() );
        imConv.setGroupId( dto.getGroupId() );
        imConv.setLastMsgId( dto.getLastMsgId() );
        imConv.setCreateTime( dto.getCreateTime() );
        imConv.setUpdateTime( dto.getUpdateTime() );

        return imConv;
    }

    @Override
    public ImConvDTO toDto(ImConv entity) {
        if ( entity == null ) {
            return null;
        }

        ImConvDTO imConvDTO = new ImConvDTO();

        imConvDTO.setId( entity.getId() );
        imConvDTO.setUser1Id( entity.getUser1Id() );
        imConvDTO.setUser2Id( entity.getUser2Id() );
        imConvDTO.setGroupId( entity.getGroupId() );
        imConvDTO.setLastMsgId( entity.getLastMsgId() );
        imConvDTO.setCreateTime( entity.getCreateTime() );
        imConvDTO.setUpdateTime( entity.getUpdateTime() );

        return imConvDTO;
    }

    @Override
    public List<ImConv> toEntity(List<ImConvDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<ImConv> list = new ArrayList<ImConv>( dtoList.size() );
        for ( ImConvDTO imConvDTO : dtoList ) {
            list.add( toEntity( imConvDTO ) );
        }

        return list;
    }

    @Override
    public List<ImConvDTO> toDto(List<ImConv> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ImConvDTO> list = new ArrayList<ImConvDTO>( entityList.size() );
        for ( ImConv imConv : entityList ) {
            list.add( toDto( imConv ) );
        }

        return list;
    }
}
