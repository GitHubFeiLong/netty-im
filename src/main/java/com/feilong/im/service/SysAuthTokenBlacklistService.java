package com.feilong.im.service;

import com.feilong.im.entity.SysAuthTokenBlacklist;
import com.feilong.im.dto.SysAuthTokenBlacklistDTO;
import com.feilong.im.dto.bo.SysAuthTokenBlacklistBO;
import com.feilong.im.dto.vo.SysAuthTokenBlacklistVO;
import com.feilong.im.dto.form.SysAuthTokenBlacklistForm;
import com.feilong.im.dto.form.SysAuthTokenBlacklistSaveForm;
import com.feilong.im.dto.form.SysAuthTokenBlacklistUpdateForm;
import com.feilong.im.dto.page.query.SysAuthTokenBlacklistPageQuery;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 认证token 黑名单 服务类接口
 * @author cfl 2026/04/16
 */
public interface SysAuthTokenBlacklistService extends IService<SysAuthTokenBlacklist> {

    /**
     * 认证token 黑名单分页查询
     * @param pageQuery 认证token 黑名单分页查询参数
     * @return 分页结果
     */
    IPage<SysAuthTokenBlacklistVO> page(SysAuthTokenBlacklistPageQuery pageQuery);

    /**
     * 获取认证token 黑名单表单数据
     * @param id 认证token 黑名单ID
     * @return 认证token 黑名单表单数据
     */
     SysAuthTokenBlacklistForm getForm(Long id);

     /**
      * 新增认证token 黑名单
      * @param formData 认证token 黑名单Save表单对象
      * @return true-成功，false-失败
      */
     SysAuthTokenBlacklist save(SysAuthTokenBlacklistSaveForm formData);

    /**
     * 保存黑名单token ID，如果存在不进行操作，如果不存在就保存
     * @param id token ID
     * @param token token
     * @return 保存结果
     */
    boolean save(String id, String token);

    /**
     * 修改认证token 黑名单
     * @param id 认证token 黑名单ID
     * @param formData 认证token 黑名单Update表单对象
     * @return true-成功，false-失败
     */
    SysAuthTokenBlacklist update(Long id, SysAuthTokenBlacklistUpdateForm formData);

    /**
     * 删除认证token 黑名单
     * @param ids 认证token 黑名单ID，多个以英文逗号(,)分割
     * @return true-成功，false-失败
     */
    boolean delete(String ids);
}
