package com.feilong.im.context;

import com.feilong.im.config.NettyServerHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * 当前用户上下文
 * @author cfl 2026/04/08
 */
public class CurrentUserContext {
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
}
