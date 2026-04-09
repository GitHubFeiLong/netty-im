package com.feilong.im.core;

import lombok.Data;

/**
 * @author cfl 2026/04/09
 */
@Data
public class Result<T> {

    private String code;

    private String msg;

    private T data;
}
