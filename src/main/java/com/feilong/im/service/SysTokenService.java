package com.feilong.im.service;

import com.feilong.im.entity.SysToken;
import com.feilong.im.dto.SysTokenDTO;
import com.feilong.im.dto.bo.SysTokenBO;
import com.feilong.im.dto.vo.SysTokenVO;
import com.feilong.im.dto.form.SysTokenForm;
import com.feilong.im.dto.form.SysTokenSaveForm;
import com.feilong.im.dto.form.SysTokenUpdateForm;
import com.feilong.im.dto.page.query.SysTokenPageQuery;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 认证用户TOKEN 服务类接口
 * @author cfl 2026/04/23
 */
public interface SysTokenService extends IService<SysToken> {

    /**
     * 认证用户TOKEN分页查询
     * @param pageQuery 认证用户TOKEN分页查询参数
     * @return 分页结果
     */
    IPage<SysTokenVO> page(SysTokenPageQuery pageQuery);

    /**
     * 获取认证用户TOKEN表单数据
     * @param id 认证用户TOKENID
     * @return 认证用户TOKEN表单数据
     */
     SysTokenForm getForm(Long id);

     /**
      * 新增认证用户TOKEN
      * @param formData 认证用户TOKENSave表单对象
      * @return true-成功，false-失败
      */
     SysToken save(SysTokenSaveForm formData);

    /**
     * 修改认证用户TOKEN
     * @param id 认证用户TOKENID
     * @param formData 认证用户TOKENUpdate表单对象
     * @return true-成功，false-失败
     */
    SysToken update(Long id, SysTokenUpdateForm formData);

    /**
     * 删除认证用户TOKEN
     * @param ids 认证用户TOKENID，多个以英文逗号(,)分割
     * @return true-成功，false-失败
     */
    boolean delete(String ids);

}
