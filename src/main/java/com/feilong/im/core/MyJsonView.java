package com.feilong.im.core;

/**
 * jackson 视图
 * @author cfl 2026/04/09
 */
public class MyJsonView {

    /**
     * 基础视图
     */
    public interface Base {}

    /**
     * 简单视图 - 包含基础视图
     */
    public interface Simple extends Base {}

    /**
     * 详细视图 - 包含简单视图
     */
    public interface Detail extends Simple {}
}
