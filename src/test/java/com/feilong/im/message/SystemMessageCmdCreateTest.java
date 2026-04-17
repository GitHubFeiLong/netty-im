package com.feilong.im.message;

import com.feilong.im.enums.MessageTypeEnum;
import com.feilong.im.enums.cmd.MessageCmdSystemEnum;
import com.feilong.im.handler.netty.cmd.req.SystemAuthReq;
import com.feilong.im.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author cfl 2026/04/14
 */
@ExtendWith({})
@Slf4j
public class SystemMessageCmdCreateTest {

    /**
     * 创建认证请求
     */
    @Test
    void testCreateAuthReq() {
        MessageReq messageReq = MessageReq.create(MessageTypeEnum.SYSTEM, MessageCmdSystemEnum.AUTH_REQ);
        SystemAuthReq req = new SystemAuthReq();
        req.setToken("eyJhbGciOiJIUzI1NiJ9.eyJqd3RVc2VyRGV0YWlsIjoie1widG9rZW5JZFwiOlwiNWFkZTM0NDU2M2I1NDZlN2IxMzhmMDcwOTNkNWU2M2RcIixcImlkXCI6XCIxXCIsXCJ1c2VybmFtZVwiOlwiYWRtaW5cIn0iLCJqd3RVc2VyVHlwZSI6ImltVXNlciIsInN1YiI6ImltVXNlcl8xIiwianRpIjoiNWFkZTM0NDU2M2I1NDZlN2IxMzhmMDcwOTNkNWU2M2QiLCJpYXQiOjE3NzYzOTA0NTAsImV4cCI6NDkyOTk5MDQ1MH0.4oBST8Lq18ONFDJp32WDxaJGXOEflYF5rozGN1mdoGQ");
        messageReq.setData(JsonUtil.toJsonString(req));

        log.info("{}", JsonUtil.toJsonString(messageReq));
    }

    /**
     * 创建心跳请求
     */
    @Test
    void testCreateHeartbeatReq() {
        MessageReq messageReq = MessageReq.create(MessageTypeEnum.SYSTEM, MessageCmdSystemEnum.HEARTBEAT_REQ);
        log.info("{}", JsonUtil.toJsonString(messageReq));
    }
}
