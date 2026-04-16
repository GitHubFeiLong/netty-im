package com.feilong.im.service;

import com.feilong.im.entity.SysUser;
import com.feilong.im.dto.SysUserDTO;
import com.feilong.im.dto.bo.SysUserBO;
import com.feilong.im.dto.vo.SysUserVO;
import com.feilong.im.dto.form.SysUserForm;
import com.feilong.im.dto.form.SysUserSaveForm;
import com.feilong.im.dto.form.SysUserUpdateForm;
import com.feilong.im.dto.page.query.SysUserPageQuery;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 系统用户 服务类接口
 * @author cfl 2026/04/16
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 系统用户分页查询
     * @param pageQuery 系统用户分页查询参数
     * @return 分页结果
     */
    IPage<SysUserVO> page(SysUserPageQuery pageQuery);

    /**
     * 获取系统用户表单数据
     * @param id 系统用户ID
     * @return 系统用户表单数据
     */
     SysUserForm getForm(Long id);

     /**
      * 新增系统用户
      * @param formData 系统用户Save表单对象
      * @return true-成功，false-失败
      */
     SysUser save(SysUserSaveForm formData);

    /**
     * 修改系统用户
     * @param id 系统用户ID
     * @param formData 系统用户Update表单对象
     * @return true-成功，false-失败
     */
    SysUser update(Long id, SysUserUpdateForm formData);

    /**
     * 删除系统用户
     * @param ids 系统用户ID，多个以英文逗号(,)分割
     * @return true-成功，false-失败
     */
    boolean delete(String ids);
}
