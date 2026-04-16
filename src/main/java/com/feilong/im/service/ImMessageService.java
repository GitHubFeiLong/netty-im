package com.feilong.im.service;

import com.feilong.im.entity.ImMessage;
import com.feilong.im.dto.ImMessageDTO;
import com.feilong.im.dto.bo.ImMessageBO;
import com.feilong.im.dto.vo.ImMessageVO;
import com.feilong.im.dto.form.ImMessageForm;
import com.feilong.im.dto.form.ImMessageSaveForm;
import com.feilong.im.dto.form.ImMessageUpdateForm;
import com.feilong.im.dto.page.query.ImMessagePageQuery;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * im消息表 服务类接口
 * @author cfl 2026/04/16
 */
public interface ImMessageService extends IService<ImMessage> {

    /**
     * im消息表分页查询
     * @param pageQuery im消息表分页查询参数
     * @return 分页结果
     */
    IPage<ImMessageVO> page(ImMessagePageQuery pageQuery);

    /**
     * 获取im消息表表单数据
     * @param id im消息表ID
     * @return im消息表表单数据
     */
     ImMessageForm getForm(Long id);

     /**
      * 新增im消息表
      * @param formData im消息表Save表单对象
      * @return true-成功，false-失败
      */
     ImMessage save(ImMessageSaveForm formData);

    /**
     * 修改im消息表
     * @param id im消息表ID
     * @param formData im消息表Update表单对象
     * @return true-成功，false-失败
     */
    ImMessage update(Long id, ImMessageUpdateForm formData);

    /**
     * 删除im消息表
     * @param ids im消息表ID，多个以英文逗号(,)分割
     * @return true-成功，false-失败
     */
    boolean delete(String ids);

    /**
     * 获取未读消息数量
     * @param convId 会话ID
     * @param receiverId 接收者ID
     * @return 未读消息数量
     */
    int countUnreadMessage(Long convId, Long receiverId);
}
