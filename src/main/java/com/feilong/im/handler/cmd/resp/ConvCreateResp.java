package com.feilong.im.handler.cmd.resp;

import com.feilong.im.dto.ImConvDTO;
import lombok.Data;

/**
 * @author cfl 2026/03/31
 */
@Data
public class ConvCreateResp {

    /**
     * 会话信息
     */
    private ImConvDTO conv;
}
