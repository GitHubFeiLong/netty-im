package ${package.Service};

<#list pojoPkgs as pkg>
import ${pkg};
</#list>
import ${superServiceClassPackage};
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * ${table.comment!} 服务类接口
 * @author ${author} ${date}
 */
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    /**
     * ${table.comment!}分页查询
     * @param pageQuery ${table.comment!}分页查询参数
     * @return 分页结果
     */
    IPage<${entity}VO> page(${entity}PageQuery pageQuery);

    /**
     * 获取${table.comment!}表单数据
     * @param id ${table.comment!}ID
     * @return ${table.comment!}表单数据
     */
     ${entity}Form getForm(Long id);

     /**
      * 新增${table.comment!}
      * @param formData ${table.comment!}Save表单对象
      * @return true-成功，false-失败
      */
     ${entity} save(${entity}SaveForm formData);

    /**
     * 修改${table.comment!}
     * @param id ${table.comment!}ID
     * @param formData ${table.comment!}Update表单对象
     * @return true-成功，false-失败
     */
    ${entity} update(Long id, ${entity}UpdateForm formData);

    /**
     * 删除${table.comment!}
     * @param ids ${table.comment!}ID，多个以英文逗号(,)分割
     * @return true-成功，false-失败
     */
    boolean delete(String ids);
}
