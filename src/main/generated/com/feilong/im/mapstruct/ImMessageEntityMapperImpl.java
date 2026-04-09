package com.feilong.im.mapstruct;

import com.feilong.im.dto.ImMessageDTO;
import com.feilong.im.entity.ImMessage;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-08T11:52:55+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
@Component
public class ImMessageEntityMapperImpl implements ImMessageEntityMapper {

    @Override
    public ImMessage toEntity(ImMessageDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ImMessage imMessage = new ImMessage();

        imMessage.setId( dto.getId() );
        imMessage.setConvId( dto.getConvId() );
        imMessage.setSenderId( dto.getSenderId() );
        imMessage.setReceiverType( dto.getReceiverType() );
        imMessage.setReceiverId( dto.getReceiverId() );
        imMessage.setMsgType( dto.getMsgType() );
        imMessage.setContent( dto.getContent() );
        imMessage.setSendTime( dto.getSendTime() );
        imMessage.setStatus( dto.getStatus() );
        imMessage.setExtra( dto.getExtra() );
        imMessage.setUpdateTime( dto.getUpdateTime() );

        return imMessage;
    }

    @Override
    public ImMessageDTO toDto(ImMessage entity) {
        if ( entity == null ) {
            return null;
        }

        ImMessageDTO imMessageDTO = new ImMessageDTO();

        imMessageDTO.setId( entity.getId() );
        imMessageDTO.setConvId( entity.getConvId() );
        imMessageDTO.setSenderId( entity.getSenderId() );
        imMessageDTO.setReceiverType( entity.getReceiverType() );
        imMessageDTO.setReceiverId( entity.getReceiverId() );
        imMessageDTO.setMsgType( entity.getMsgType() );
        imMessageDTO.setContent( entity.getContent() );
        imMessageDTO.setSendTime( entity.getSendTime() );
        imMessageDTO.setStatus( entity.getStatus() );
        imMessageDTO.setExtra( entity.getExtra() );
        imMessageDTO.setUpdateTime( entity.getUpdateTime() );

        return imMessageDTO;
    }

    @Override
    public List<ImMessage> toEntity(List<ImMessageDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<ImMessage> list = new ArrayList<ImMessage>( dtoList.size() );
        for ( ImMessageDTO imMessageDTO : dtoList ) {
            list.add( toEntity( imMessageDTO ) );
        }

        return list;
    }

    @Override
    public List<ImMessageDTO> toDto(List<ImMessage> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ImMessageDTO> list = new ArrayList<ImMessageDTO>( entityList.size() );
        for ( ImMessage imMessage : entityList ) {
            list.add( toDto( imMessage ) );
        }

        return list;
    }
}
