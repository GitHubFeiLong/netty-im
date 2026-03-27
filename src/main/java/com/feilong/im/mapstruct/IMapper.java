package com.feilong.im.mapstruct;

import java.util.List;

/**
 * 为entity和dto映射器订立契约
 * @author cfl 2026/3/26
 */
public interface IMapper<E, D> {

    /**
     * dto转entity
     * @param dto   dto
     * @return entity
     */
    E toEntity(D dto);

    /**
     * entity转dto
     * @param entity    entity
     * @return dto
     */
    D toDto(E entity);

    /**
     * dto集合转entity集合
     * @param dtoList   dto集合
     * @return entity集合
     */
    List<E> toEntity(List<D> dtoList);

    /**
     * entity集合转dto集合
     * @param entityList    entity集合
     * @return dto集合
     */
    List<D> toDto(List<E> entityList);
}
