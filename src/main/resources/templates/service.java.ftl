package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
import com.baomidou.mybatisplus.core.metadata.IPage;
import ${package.Form}.${entity}Form;
import ${package.PageQuery}.${entity}PageQuery;
import ${package.VO}.${entity}PageVO;

/**
 * ${table.comment!} 服务类接口
 * @author ${author} ${date}
 */
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    /**
     * 分页列表
     * @param queryParams 分页查询参数
     * @return 分页结果
     */
    IPage<${entity}VO> listPage(${entity}PageQuery queryParams);

    /**
     * 获取表单数据
     * @param id ID
     * @return 表单数据
     */
     ${entity}Form getFormData(Long id);

    /**
     * 新增
     * @param formData 表单对象
     * @return true-成功，false-失败
     */
     ${entity} save(${entity}Form formData);

    /**
     * 修改
     * @param id ID
     * @param formData 表单对象
     * @return true-成功，false-失败
     */
    ${entity} update(Long id, ${entity}Form formData);

    /**
     * 删除
     * @param ids ID，多个以英文逗号(,)分割
     * @return true-成功，false-失败
     */
    boolean delete(String ids);
}
