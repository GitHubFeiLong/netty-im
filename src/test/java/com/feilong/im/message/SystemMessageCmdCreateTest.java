package com.feilong.im.message;

import com.feilong.im.enums.MessageTypeEnum;
import com.feilong.im.enums.cmd.MessageCmdSystemEnum;
import com.feilong.im.handler.cmd.req.SystemAuthReq;
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
     * 创建消息
     * @param typeEnum 消息类型
     * @param cmd 命令
     * @return
     */
    private MessageReq create(MessageTypeEnum typeEnum, MessageCmdSystemEnum cmd) {
        return new MessageReq()
                .setReqId(UUID.randomUUID().toString().replace("-", ""))
                .setType(typeEnum)
                .setCmd(cmd.name())
                .setTimestamp(LocalDateTime.now());
    }

    /**
     * 创建认证请求
     */
    @Test
    void testCreateAuthReq() {
        MessageReq messageReq = create(MessageTypeEnum.SYSTEM, MessageCmdSystemEnum.AUTH_REQ);
        SystemAuthReq req = new SystemAuthReq();
        req.setToken("eyJhbGciOiJIUzUxMiJ9.eyJqd3RVc2VyRGV0YWlsIjoie1wiaWRcIjpcIjFcIixcInVzZXJuYW1lXCI6XCJhZG1pblwifSIsImp3dFVzZXJUeXBlIjoiaW1Vc2VyIiwic3ViIjoiSW1Vc2VyRGV0YWlscyhpZD0xLCB1c2VybmFtZT1hZG1pbiwgbmlja25hbWU9bnVsbCkiLCJqdGkiOiI4MDAwYTI2Mi1iZmQzLTQxYWItYWExNS05YmM0NWJiMDBlNjciLCJpYXQiOjE3NzYxMzMxNjIsImV4cCI6MTc3NjczNzk2Mn0.r8dHhxgonid_2H08_mFXjnlSwun9pPQ-YalOYX55csa9cvK5DoVrfaB9ZfO6aX2u__nRJs4I52WUhkP3j9UXpg");
        messageReq.setData(JsonUtil.toJsonString(req));

        log.info("{}", JsonUtil.toJsonString(messageReq));
    }

    /**
     * 创建心跳请求
     */
    @Test
    void testCreateHeartbeatReq() {
        MessageReq messageReq = create(MessageTypeEnum.SYSTEM, MessageCmdSystemEnum.HEARTBEAT_REQ);

        log.info("{}", JsonUtil.toJsonString(messageReq));
    }
}
