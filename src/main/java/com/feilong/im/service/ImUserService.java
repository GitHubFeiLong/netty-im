package com.feilong.im.service;

import com.feilong.im.entity.ImUser;
import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.entity.ImUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.feilong.im.dto.form.ImUserForm;
import com.feilong.im.dto.form.ImUserSaveForm;
import com.feilong.im.dto.form.ImUserUpdateForm;
import com.feilong.im.dto.page.query.ImUserPageQuery;
import com.feilong.im.dto.vo.ImUserVO;

import java.util.List;

/**
 * im账户表 服务类接口
 * @author cfl 2026/04/16
 */
public interface ImUserService extends IService<ImUser> {

    /**
     * im账户表分页查询
     * @param pageQuery im账户表分页查询参数
     * @return 分页结果
     */
    IPage<ImUserVO> page(ImUserPageQuery pageQuery);

    /**
     * 获取im账户表表单数据
     * @param id im账户表ID
     * @return im账户表表单数据
     */
     ImUserForm getForm(Long id);

     /**
      * 新增im账户表
      * @param formData im账户表Save表单对象
      * @return true-成功，false-失败
      */
     ImUser save(ImUserSaveForm formData);

    /**
     * 修改im账户表
     * @param id im账户表ID
     * @param formData im账户表Update表单对象
     * @return true-成功，false-失败
     */
    ImUser update(Long id, ImUserUpdateForm formData);

    /**
     * 删除im账户表
     * @param ids im账户表ID，多个以英文逗号(,)分割
     * @return true-成功，false-失败
     */
    boolean delete(String ids);

    /**
     * 获取缓存中的用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    ImUserDTO getCatchById(Long userId);

    /**
     * 根据用户ID查询，不存在时进行创建
     * @param userId 用户ID
     * @return IM账户
     */
    ImUser getOrCreateById(Long userId);

    /**
     * 更新用户状态
     * @param id  用户ID
     * @param status  用户状态（0-离线，1-在线，2-忙碌，3-隐身）
     * @return  更新结果
     */
    boolean updateStatus(Long id, int status);

    /**
     * 根据用户ID列表查询用户信息
     * @param userIds  用户ID列表
     * @return  用户信息列表
     */
    List<ImUser> listInfoByUserIds(List<Long> userIds);
}
