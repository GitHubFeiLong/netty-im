package com.feilong.im.service;

import com.feilong.im.entity.ImConv;
import com.feilong.im.dto.ImConvDTO;
import com.feilong.im.dto.bo.ImConvBO;
import com.feilong.im.dto.vo.ImConvVO;
import com.feilong.im.dto.form.ImConvForm;
import com.feilong.im.dto.form.ImConvSaveForm;
import com.feilong.im.dto.form.ImConvUpdateForm;
import com.feilong.im.dto.page.query.ImConvPageQuery;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * im会话表 服务类接口
 * @author cfl 2026/04/16
 */
public interface ImConvService extends IService<ImConv> {

    /**
     * im会话表分页查询
     * @param pageQuery im会话表分页查询参数
     * @return 分页结果
     */
    IPage<ImConvVO> page(ImConvPageQuery pageQuery);

    /**
     * 获取im会话表表单数据
     * @param id im会话表ID
     * @return im会话表表单数据
     */
     ImConvForm getForm(Long id);

     /**
      * 新增im会话表
      * @param formData im会话表Save表单对象
      * @return true-成功，false-失败
      */
     ImConv save(ImConvSaveForm formData);

    /**
     * 修改im会话表
     * @param id im会话表ID
     * @param formData im会话表Update表单对象
     * @return true-成功，false-失败
     */
    ImConv update(Long id, ImConvUpdateForm formData);

    /**
     * 删除im会话表
     * @param ids im会话表ID，多个以英文逗号(,)分割
     * @return true-成功，false-失败
     */
    boolean delete(String ids);

    /**
     * 获取缓存中的用户信息
     * @param minUserId 较小的用户ID
     * @param maxUserId 较大的用户ID
     * @return 用户信息
     */
    ImConvDTO getSingleCatchById(Long minUserId, Long maxUserId);

    // /**
    //  * 根据会话ID查询会话信息
    //  * @param convId 会话ID
    //  * @return 会话信息
    //  */
    // ImConvDTO getCatchById(Long convId);
    //
    // /**
    //  * 更新会话最后一条消息ID
    //  * @param convId    会话ID
    //  * @param lastMsgId 消息ID
    //  * @return 更新结果
    //  */
    // boolean updateLastMsgId(Long convId, Long lastMsgId);
    //
    // /**
    //  * 查询用户相关的指定类型的所有会话ID
    //  * @param userId 用户ID
    //  * @param convType 会话类型
    //  * @return 会话ID集合
    //  */
    // Set<Long> queryConvIdsByUserId(Long userId, ImConvTypeEnum convType);
}
