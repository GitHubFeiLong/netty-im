package com.feilong.im.service;

import com.feilong.im.entity.ImConvUser;
import com.feilong.im.dto.ImConvUserDTO;
import com.feilong.im.dto.bo.ImConvUserBO;
import com.feilong.im.dto.vo.ImConvUserVO;
import com.feilong.im.dto.form.ImConvUserForm;
import com.feilong.im.dto.form.ImConvUserSaveForm;
import com.feilong.im.dto.form.ImConvUserUpdateForm;
import com.feilong.im.dto.page.query.ImConvUserPageQuery;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * im用户会话表 服务类接口
 * @author cfl 2026/04/16
 */
public interface ImConvUserService extends IService<ImConvUser> {

    /**
     * im用户会话表分页查询
     * @param pageQuery im用户会话表分页查询参数
     * @return 分页结果
     */
    IPage<ImConvUserVO> page(ImConvUserPageQuery pageQuery);

    /**
     * 获取im用户会话表表单数据
     * @param id im用户会话表ID
     * @return im用户会话表表单数据
     */
     ImConvUserForm getForm(Long id);

     /**
      * 新增im用户会话表
      * @param formData im用户会话表Save表单对象
      * @return true-成功，false-失败
      */
     ImConvUser save(ImConvUserSaveForm formData);

    /**
     * 修改im用户会话表
     * @param id im用户会话表ID
     * @param formData im用户会话表Update表单对象
     * @return true-成功，false-失败
     */
    ImConvUser update(Long id, ImConvUserUpdateForm formData);

    /**
     * 删除im用户会话表
     * @param ids im用户会话表ID，多个以英文逗号(,)分割
     * @return true-成功，false-失败
     */
    boolean delete(String ids);

    /**
     * 获取或创建会话用户
     * @param convId 会话ID
     * @param userId 用户ID
     * @return 会话用户
     */
    ImConvUser getOrCreate(Long convId, Long userId);
}
