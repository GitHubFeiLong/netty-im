package com.feilong.im.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.entity.ImFriend;
import com.feilong.im.dto.ImFriendDTO;
import com.feilong.im.dto.bo.ImFriendBO;
import com.feilong.im.dto.vo.ImFriendVO;
import com.feilong.im.dto.form.ImFriendForm;
import com.feilong.im.dto.form.ImFriendSaveForm;
import com.feilong.im.dto.form.ImFriendUpdateForm;
import com.feilong.im.dto.page.query.ImFriendPageQuery;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.feilong.im.handler.netty.cmd.req.ContactPageReq;

/**
 * 用户好友表 服务类接口
 * @author cfl 2026/04/16
 */
public interface ImFriendService extends IService<ImFriend> {

    /**
     * 用户好友表分页查询
     * @param pageQuery 用户好友表分页查询参数
     * @return 分页结果
     */
    IPage<ImFriendVO> page(ImFriendPageQuery pageQuery);

    /**
     * 获取用户好友表表单数据
     * @param id 用户好友表ID
     * @return 用户好友表表单数据
     */
     ImFriendForm getForm(Long id);

     /**
      * 新增用户好友表
      * @param formData 用户好友表Save表单对象
      * @return true-成功，false-失败
      */
     ImFriend save(ImFriendSaveForm formData);

    /**
     * 修改用户好友表
     * @param id 用户好友表ID
     * @param formData 用户好友表Update表单对象
     * @return true-成功，false-失败
     */
    ImFriend update(Long id, ImFriendUpdateForm formData);

    /**
     * 删除用户好友表
     * @param ids 用户好友表ID，多个以英文逗号(,)分割
     * @return true-成功，false-失败
     */
    boolean delete(String ids);

    /**
     * 查询用户联系人列表
     * @param imUserId 用户ID
     * @param pageReq 分页参数
     * @return 联系人列表
     */
    Page<ImUserDTO> listPage(Long imUserId, ContactPageReq pageReq);

}
