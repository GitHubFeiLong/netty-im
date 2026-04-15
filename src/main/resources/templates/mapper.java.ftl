package ${package.Mapper};

<#list importMapperFrameworkPackages as pkg>
import ${pkg};
</#list>
<#if importMapperJavaPackages?size !=0>
  <#list importMapperJavaPackages as pkg>
import ${pkg};
   </#list>
</#if>
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${package.BO}.${entity}BO;
import ${package.PageQuery}.${entity}PageQuery;

/**
 * ${table.comment!} Mapper 接口
 * @author ${author} ${date}
 */
<#if mapperAnnotationClass??>
@${mapperAnnotationClass.simpleName}
</#if>
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}> {
<#else>
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {
</#if>

<#list mapperMethodList as m>
    /**
     * generate by ${m.indexName}
     *
    <#list m.tableFieldList as f>
     * @param ${f.propertyName} ${f.comment}
    </#list>
     */
    ${m.method}
</#list>

    /**
     * ${table.comment!} 分页查询
     * @param page 分页参数
     * @param pageQuery 查询参数
     * @return ${table.comment!}分页结果
     */
    Page<${entity}BO> page(Page<${entity}BO> page, ${entity}PageQuery pageQuery);
}
