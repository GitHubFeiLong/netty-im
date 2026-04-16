package ${package.EntityMapper};

<#list pojoPkgs as pkg>
import ${pkg};
</#list>

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * ${table.comment} EntityMapper
 * @see ${package.Entity}.${entity}
 * @author ${author} ${date}
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ${entity}EntityMapper extends EntityMapper<${entity}, ${entity}DTO> {

    /**
     * 实体转表单
     * @param entity 实体
     * @return 实体对应的表单对象
     */
    ${entity}Form toForm(${entity} entity);

    /**
     * 表单转实体
     * @param form 表单对象
     * @return 表单对应的实体对象
     */
    ${entity} toEntity(${entity}Form form);

    /**
     * Save表单转实体
     * @param saveForm 表单对象
     * @return 表单对应的实体对象
     */
    ${entity} toEntity(${entity}SaveForm saveForm);

    /**
     * Update表单转实体
     * @param updateForm 表单对象
     * @return 表单对应的实体对象
     */
    ${entity} toEntity(${entity}UpdateForm updateForm);

    /**
     * 实体转VO
     * @param entity 实体
     * @return VO
     */
    ${entity}VO toVo(${entity} entity);

    /**
     * 实体集合转VO集合
     * @param entityList 实体集合
     * @return VO集合
     */
    List<${entity}VO> toVo(Collection<${entity}> entityList);

    /**
     * DTO转Form
     * @param dto 实体DTO
     * @return 表单对象
     */
    ${entity}Form toForm(${entity}DTO dto);

    /**
     * DTO转VO
     * @param dto 实体DTO
     * @return VO
     */
    ${entity}VO toVo(${entity}DTO dto);

    /**
    * 分页结果转换
    * @param bo 源分页信息
    * @return 目标分页信息
    */
    Page<${entity}VO> toVo(Page<${entity}BO> bo);
}
