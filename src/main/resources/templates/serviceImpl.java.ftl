package ${package.ServiceImpl};

<#list pojoPkgs as pkg>
import ${pkg};
</#list>
<#if generateService>
import ${package.Service}.${table.serviceName};
</#if>
import ${package.Mapper}.${table.mapperName};
import ${superServiceImplClassPackage};
import ${package.EntityMapper}.${entity}EntityMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feilong.im.exception.ClientException;
import com.feilong.im.util.AssertUtil;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

/**
 * ${table.comment!} 服务接口实现类
 * @author ${author} ${date}
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}><#if generateService> implements ${table.serviceName}</#if> {

    private final ${entity}Mapper ${entity?uncap_first}Mapper;
    private final ${entity}EntityMapper ${entity?uncap_first}EntityMapper;

    /**
     * ${table.comment!}分页查询
     *
     * @param pageQuery ${table.comment!}分页查询参数
     * @return 分页结果
     */
    @Override
    public IPage<${entity}VO> page(${entity}PageQuery pageQuery) {
        log.debug("分页查询${table.name}，查询参数：{}", pageQuery);
        // 参数构建
        Page<${entity}BO> page = new Page<>(pageQuery.getPage(), pageQuery.getSize());

        // 查询数据
        Page<${entity}BO> boPage = ${entity?uncap_first}Mapper.page(page, pageQuery);

        // 实体转换
        return ${entity?uncap_first}EntityMapper.toVo(boPage);
    }

    /**
     * 获取${table.comment!}表单数据
     *
     * @param id ${table.comment!}ID
     * @return ${table.comment!}表单数据
     */
    @Override
    public ${entity}Form getForm(Long id) {
        log.debug("获取${table.name}表单数据：{}", id);
        ${entity} entity = this.getById(id);
        return ${entity?uncap_first}EntityMapper.toForm(entity);
    }

    /**
     * 新增${table.comment!}
     *
     * @param formData ${table.comment!}Save表单对象
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ${entity} save(${entity}SaveForm formData) {
        log.debug("新增${table.name}数据：{}", formData);
        // 实体转换 form->entity
        ${entity} entity = ${entity?uncap_first}EntityMapper.toEntity(formData);
    <#if LOGIC_DELETE_COLUMN_NAME??>
        entity.set${LOGIC_DELETE_COLUMN_NAME?cap_first}(${LOGIC_DELETE_DEFAULT_VALUE});
    </#if>
        boolean flag = this.save(entity);
        if (flag) {
            log.debug("新增${table.name}数据成功：{}", entity);
            return entity;
        }
        log.warn("新增${table.name}数据失败");
        throw ClientException.of("保存数据失败，请稍后再试").setServerMessage("保存表${table.name}失败");
    }

    /**
     * 修改${table.comment!}
     *
     * @param id ${table.comment!}ID
     * @param formData ${table.comment!}Update表单对象
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ${entity} update(Long id, ${entity}UpdateForm formData) {
        log.debug("修改${table.name}，ID：{}，表单数据：{}", id, formData);
        ${entity} entity = ${entity?uncap_first}EntityMapper.toEntity(formData);

        boolean flag = this.updateById(entity);
        if (flag) {
            log.debug("修改${table.name}成功：{}", entity);
            return entity;
        }
        log.warn("修改${table.name}数据失败");
        throw ClientException.of("保存数据失败，请稍后再试").setServerMessage("修改表${table.name}失败");
    }

    /**
     * 删除${table.comment!}
     *
     * @param ids ${table.comment!}ID，多个以英文逗号(,)分割
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String ids) {
        log.debug("删除${table.name}数据：{}", ids);
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return this.removeByIds(idList);
    }
}
