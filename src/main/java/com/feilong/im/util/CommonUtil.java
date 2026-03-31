package com.feilong.im.util;

import com.feilong.im.config.TraceIdHandler;
import com.feilong.im.context.TraceIdContext;
import io.netty.channel.Channel;

import java.util.Optional;

/**
 * 通用工具类
 * @author cfl 2026/02/28
 */
public class CommonUtil {

    /**
     * 根据是否是官方客服店铺生成店铺的IM昵称
     * @param isOwnOfficialShopId 是否是官方客服店铺ID，true-是；false-不是
     * @param stationCode   店铺站点号
     * @return 店铺的IM昵称
     */
    public static String generateNicknameByShop(boolean isOwnOfficialShopId, String stationCode) {
        return isOwnOfficialShopId ? "官方客服" : "站点号：" + stationCode;
    }

    /**
     * 获取channel上的traceId属性
     * @param currentChannel channel
     * @return traceId
     */
    public static String getTraceId(Channel currentChannel) {
        return currentChannel.attr(TraceIdHandler.TRACE_ID_KEY).get();
    }

    /**
     * 生成子线程的traceId
     * @param currentChannel channel
     * @return traceId
     */
    public static String genSubThreadTraceId(Channel currentChannel) {
        String traceId = Optional.ofNullable(currentChannel.attr(TraceIdHandler.TRACE_ID_KEY).get()).orElseGet(() -> "");
        return traceId + "-" + TraceIdContext.generateTraceId(4);
    }

}
