package com.feilong.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feilong.im.entity.SysAuthTokenBlacklist;

/**
 * @author cfl 2026/04/13
 */
public interface SysAuthTokenBlacklistService extends IService<SysAuthTokenBlacklist> {

    /**
     * 保存黑名单token ID，如果存在不进行操作，如果不存在就保存
     * @param id token ID
     * @param token token
     * @return 保存结果
     */
    boolean save(String id, String token);

}
