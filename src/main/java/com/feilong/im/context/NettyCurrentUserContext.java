package com.feilong.im.context;

import com.feilong.im.handler.netty.NettyServerHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

/**
 * Netty当前用户上下文
 * @author cfl 2026/04/08
 */
public class NettyCurrentUserContext {
    private static final InheritableThreadLocal<CurrentUser> THREAD_LOCAL = new InheritableThreadLocal<>();

    public static void set(CurrentUser currentUser) {
        THREAD_LOCAL.set(currentUser);
    }

    public static void set(ChannelHandlerContext ctx) {
        Long userId = NettyServerHandler.channelUserMap.get(ctx.channel());
        CurrentUser currentUser = new CurrentUser();
        currentUser.setUserId(userId);
        THREAD_LOCAL.set(currentUser);
    }

    public static CurrentUser get() {
        return THREAD_LOCAL.get();
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

    /**
     * 当前用户信息
     * @author cfl 2026/04/08
     */
    @Data
    public static class CurrentUser {
        /**
         * JWT ID
         */
        private String jwtId;

        /**
         * 用户ID
         */
        private Long userId;
    }
}
