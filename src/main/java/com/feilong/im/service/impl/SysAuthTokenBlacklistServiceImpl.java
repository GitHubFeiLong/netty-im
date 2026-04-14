package com.feilong.im.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feilong.im.dao.SysAuthTokenBlacklistMapper;
import com.feilong.im.entity.SysAuthTokenBlacklist;
import com.feilong.im.service.SysAuthTokenBlacklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author cfl 2026/04/13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysAuthTokenBlacklistServiceImpl extends ServiceImpl<SysAuthTokenBlacklistMapper, SysAuthTokenBlacklist> implements SysAuthTokenBlacklistService {

    /**
     * 保存黑名单token ID，如果存在不进行操作，如果不存在就保存
     *
     * @param id token ID
     * @param token token
     * @return 保存结果
     */
    @Override
    public boolean save(String id, String token) {
        SysAuthTokenBlacklist sysAuthTokenBlacklist = super.getById(id);
        if (sysAuthTokenBlacklist != null) {
            return false;
        }

        synchronized (this) {
            sysAuthTokenBlacklist = super.getById(id);
            if (sysAuthTokenBlacklist != null) {
                return false;
            }

            sysAuthTokenBlacklist = new SysAuthTokenBlacklist();
            sysAuthTokenBlacklist.setDeleted(0L);
            sysAuthTokenBlacklist.setId(id);
            sysAuthTokenBlacklist.setToken(token);
            return super.save(sysAuthTokenBlacklist);
        }
    }
}
