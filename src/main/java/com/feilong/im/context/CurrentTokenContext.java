package com.feilong.im.context;

/**
 * 当前token上下文
 * @author cfl 2026/04/14
 */
public class CurrentTokenContext {

    private static final InheritableThreadLocal<String> THREAD_LOCAL = new InheritableThreadLocal<>();


    public static void set(String token) {
        THREAD_LOCAL.set(token);
    }

    public static String get() {
        return THREAD_LOCAL.get();
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
