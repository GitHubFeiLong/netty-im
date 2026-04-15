package com.feilong.im.handler.cmd.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author cfl 2026/03/30
 */
@Data
public class SystemAuthReq {

    /**
     * token
     */
    @NotBlank
    private String token;
}
