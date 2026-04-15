package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
<#if generateService>
import ${package.Service}.${table.serviceName};
</#if>
<#list pojoPkgs as pkg>
import ${pkg};
</#list>
import ${superServiceImplClassPackage};
import ${package.Mapper}.${entity}Mapper;
import ${package.EntityMapper}.${entity}EntityMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feilong.im.exception.ServerException;
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

    private final ${entity}Mapper ${firstCharLowerCaseEntity}Mapper;
    private final ${entity}EntityMapper ${firstCharLowerCaseEntity}EntityMapper;

    /**
     * ${table.comment!}分页查询
     *
     * @param pageQuery ${table.comment!}分页查询参数
     * @return 分页结果
     */
    @Override
    public IPage<${entity}VO> page(${entity}PageQuery pageQuery) {
        // 参数构建
        Page<${entity}BO> page = new Page<>(pageQuery.getPage(), pageQuery.getSize());

        // 查询数据
        Page<${entity}BO> boPage = ${firstCharLowerCaseEntity}Mapper.page(page, pageQuery);

        // 实体转换
        return ${firstCharLowerCaseEntity}EntityMapper.toVo(boPage);
    }

    /**
     * 获取${table.comment!}表单数据
     *
     * @param id ${table.comment!}ID
     * @return ${table.comment!}表单数据
     */
    @Override
    public ${entity}Form getForm(Long id) {
        ${entity} entity = this.getById(id);
        return ${firstCharLowerCaseEntity}EntityMapper.toForm(entity);
    }

    /**
     * 新增${table.comment!}
     *
     * @param formData ${table.comment!}表单对象
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ${entity} save(${entity}Form formData) {
        // 实体转换 form->entity
        ${entity} entity = ${firstCharLowerCaseEntity}EntityMapper.toEntity(formData);
        boolean flag = this.save(entity);
        if (flag) {
            return entity;
        }
        throw ServerException.of("保存${table.comment!}数据失败");
    }


    /**
     * 修改${table.comment!}
     *
     * @param id ${table.comment!}ID
     * @param formData ${table.comment!}表单对象
     * @return true-成功，false-失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ${entity} update(Long id, ${entity}Form formData) {
       ${entity} entity = ${firstCharLowerCaseEntity}EntityMapper.toEntity(formData);

        boolean flag = this.updateById(entity);
        if (flag) {
            return entity;
        }
        throw ServerException.of("修改${table.comment!}数据失败");
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
        AssertUtil.isNotBlank(ids, "删除的${table.comment!}数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return this.removeByIds(idList);
    }
}
