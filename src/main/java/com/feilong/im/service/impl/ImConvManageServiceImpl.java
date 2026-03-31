package com.feilong.im.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.feilong.im.context.CurrentTimeContext;
import com.feilong.im.dto.ImConvDTO;
import com.feilong.im.entity.ImConv;
import com.feilong.im.entity.ImConvUser;
import com.feilong.im.enums.type.ImConvTypeEnum;
import com.feilong.im.mapstruct.ImConvEntityMapper;
import com.feilong.im.service.ImConvManageService;
import com.feilong.im.service.ImConvService;
import com.feilong.im.service.ImConvUserService;
import com.feilong.im.service.ImUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 会话相关管理服务
 * @author cfl 2026/02/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImConvManageServiceImpl implements ImConvManageService {
    private final ImConvService imConvService;
    private final ImConvUserService imConvUserService;
    private final ImUserService imUserService;
    // private final ImGroupService imGroupService;
    // private final ImMessageService imMessageService;
    //
    private final ImConvEntityMapper imConvEntityMapper;
    // private final ImConvUserEntityMapper imConvUserEntityMapper;
    // private final ImUserEntityMapper imUserEntityMapper;
    // private final ImGroupEntityMapper imGroupEntityMapper;
    // private final ImMessageEntityMapper imMessageEntityMapper;


    // /**
    //  * 获取会话详情
    //  *
    //  * @param convId 会话ID
    //  * @return 会话详情
    //  */
    // @Override
    // public ImConvDTO getConvDetail(Long convId) {
    //     return imConvService.getCatchById(convId);
    // }
    //

    /**
     * 创建单聊会话
     *
     * @param minUserId 较小的用户ID
     * @param maxUserId 较大的用户ID
     * @return 会话
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImConvDTO createSingleConv(Long minUserId, Long maxUserId) {
        log.info("创建单聊会话，用户1ID：{}，用户2ID：{}", minUserId, maxUserId);

        // 校验用户之间是否存在会话
        ImConvDTO imConvDTO = imConvService.getSingleCatchById(minUserId, maxUserId);
        LocalDateTime now = CurrentTimeContext.get();
        if (imConvDTO == null) {
            log.debug("用户{}和用户{}不存在会话，即将创建单聊会话", minUserId, maxUserId);
            ImConv imConv = new ImConv();
            imConv.setType(ImConvTypeEnum.SINGLE.getId());
            imConv.setUser1Id(minUserId);
            imConv.setUser2Id(maxUserId);
            imConv.setCreateTime(now);
            imConv.setUpdateTime(now);
            imConvService.save(imConv);

            log.info("用户1：{}和用户2：{}加入到该会话", minUserId, maxUserId);
            ImConvUser convUser1 = new ImConvUser();
            convUser1.setConvId(imConv.getId());
            convUser1.setUserId(minUserId);
            convUser1.setUnreadCount(0);
            convUser1.setCreateTime(now);
            convUser1.setUpdateTime(now);

            ImConvUser convUser2 = new ImConvUser();
            convUser2.setConvId(imConv.getId());
            convUser2.setUserId(maxUserId);
            convUser2.setUnreadCount(0);
            convUser2.setCreateTime(now);
            convUser2.setUpdateTime(now);

            imConvUserService.saveBatch(Arrays.asList(convUser1, convUser2));

            ImConvDTO singleCatchById = imConvService.getSingleCatchById(minUserId, maxUserId);
            return singleCatchById;
        }

        log.debug("会话存在，查询用户1{}和用户2{}是否加入了当前会话", minUserId, maxUserId);
        // 查询用户1的会话是否存在
        imConvUserService.getOrCreate(imConvDTO.getId(), minUserId);
        // 查询用户2的会话是否存在
        imConvUserService.getOrCreate(imConvDTO.getId(), maxUserId);
        return imConvDTO;
    }
    //
    // /**
    //  * 根据用户1id查询会话列表
    //  *
    //  * @param userId 用户1id
    //  * @return 会话列表
    //  */
    // @Override
    // public List<ImConvDTO> listConvListByUserId(Long userId) {
    //     log.debug("查询用户{}的会话列表", userId);
    //     // 查询用户的所有会话列表
    //     List<ImConvUser> convUsers = imConvUserService.lambdaQuery().eq(ImConvUser::getUserId, userId).list();
    //     if (CollectionUtils.isEmpty(convUsers)) {
    //         log.debug("会话列表为空");
    //         return Collections.emptyList();
    //     }
    //     // 转DTO
    //     List<ImConvUserDTO> convUserDTOList = imConvUserEntityMapper.toDto(convUsers);
    //     Map<Long, ImConvUserDTO> longImConvUserDTOMap = convUserDTOList.stream().collect(Collectors.toMap(ImConvUserDTO::getConvId, v -> v));
    //
    //     // 查询会话信息
    //     List<ImConv> convs = imConvService.listByIds(convUserDTOList.stream().map(ImConvUserDTO::getConvId).collect(Collectors.toList()));
    //     List<ImConvDTO> convList = imConvEntityMapper.toDto(convs);
    //
    //     // 筛选单聊会话的对方用户ID
    //     List<Long> user2IdList = convList.stream()
    //             .filter(conv -> Objects.equals(conv.getConvType(), ImConvTypeEnum.SINGLE.getId()))
    //             // 对方的用户ID
    //             .map(m -> Objects.equals(userId, m.getUser1Id()) ? m.getUser2Id() : m.getUser1Id())
    //             .filter(Objects::nonNull).collect(Collectors.toList());
    //     // 筛选群聊会话的群ID
    //     List<Long> groupIdList = convList.stream()
    //             .filter(conv -> Objects.equals(conv.getConvType(), ImConvTypeEnum.GROUP.getId()))
    //             .map(ImConvDTO::getGroupId).filter(Objects::nonNull).collect(Collectors.toList());
    //     // 筛选会话的最后一条聊天消息ID
    //     List<Long> lastMessageIdList = convList.stream().map(ImConvDTO::getLastMsgId).collect(Collectors.toList());
    //
    //     // 查询user1信息
    //     ImUserDTO currentImUserDTO = imUserService.getCatchById(userId);
    //
    //     // 查询信息，将其放进map
    //     Map<Long, ImUserDTO> userMap = new HashMap<>(user2IdList.size());
    //     Map<Long, ImGroupDTO> groupMap = new HashMap<>(groupIdList.size());
    //     Map<Long, ImMessageDTO> messageMap = new HashMap<>(lastMessageIdList.size());
    //     if (!user2IdList.isEmpty()) {
    //         log.debug("用户会话存在单聊，查询单聊会话相关信息");
    //         List<ImUser> imUsers = imUserService.listInfoByUserIds(user2IdList);
    //         List<ImUserDTO> userDTOList = imUserEntityMapper.toDto(imUsers);
    //
    //         userMap.putAll(userDTOList.stream().collect(Collectors.toMap(ImUserDTO::getId, v -> v)));
    //     }
    //
    //     if (!groupIdList.isEmpty()) {
    //         log.debug("用户会话存在群聊，查询群聊会话相关信息");
    //         List<ImGroup> imGroups = imGroupService.lambdaQuery().select(ImGroup::getId, ImGroup::getName, ImGroup::getAvatar, ImGroup::getStatus).in(ImGroup::getId, groupIdList).list();
    //         List<ImGroupDTO> groupDTOList = imGroupEntityMapper.toDto(imGroups);
    //
    //         groupMap.putAll(groupDTOList.stream().collect(Collectors.toMap(ImGroupDTO::getId, v -> v)));
    //     }
    //
    //     if (!lastMessageIdList.isEmpty()) {
    //         log.debug("查询最后一条聊天信息");
    //         List<ImMessage> messages = imMessageService.lambdaQuery().in(ImMessage::getId, lastMessageIdList).list();
    //         List<ImMessageDTO> messageDTOList = imMessageEntityMapper.toDto(messages);
    //         messageMap.putAll(messageDTOList.stream().collect(Collectors.toMap(ImMessageDTO::getId, v -> v)));
    //
    //         // 先获取所有发送者用户ID集合
    //         Set<Long> senderIdSet = messageDTOList.stream().map(ImMessageDTO::getSenderId).collect(Collectors.toSet());
    //         senderIdSet.removeAll(userMap.keySet());
    //         // 还有没有查询的用户ID，查询用户信息
    //         if (!senderIdSet.isEmpty()) {
    //             log.debug("查询发送会话最后一条消息的用户信息");
    //             // 查询用户信息
    //             List<ImUser> imUsers = imUserService.listInfoByUserIds(user2IdList);
    //             List<ImUserDTO> userDTOList = imUserEntityMapper.toDto(imUsers);
    //
    //             userMap.putAll(userDTOList.stream().collect(Collectors.toMap(ImUserDTO::getId, v -> v)));
    //         }
    //
    //         // 设置消息发送者信息
    //         messageDTOList.forEach(p -> {
    //             p.setSender(userMap.get(p.getId()));
    //         });
    //     }
    //
    //     // 填充用户和群信息
    //     convList.forEach(conv -> {
    //         if (Objects.equals(conv.getConvType(), ImConvTypeEnum.SINGLE.getId())) {
    //             long user1Id = conv.getUser1Id();
    //             long user2Id = conv.getUser2Id();
    //
    //             // 修改user1和user2信息，user1是自己的信息
    //             conv.setUser1(currentImUserDTO);
    //             conv.setUser1Id(conv.getUser1().getId());
    //             // 设置对方的信息
    //             long otherUserId = Objects.equals(userId, user1Id) ? user2Id : user1Id;
    //             conv.setUser2(userMap.get(otherUserId));
    //             conv.setUser2Id(conv.getUser2().getId());
    //         } else if (Objects.equals(conv.getConvType(), ImConvTypeEnum.GROUP.getId())) {
    //             conv.setGroup(groupMap.get(conv.getGroupId()));
    //         }
    //         // 填充最后一条消息
    //         conv.setLastMsg(messageMap.get(conv.getLastMsgId()));
    //         // 设置未读消息数量
    //         if (longImConvUserDTOMap.containsKey(conv.getId())) {
    //             conv.setUnreadCount(longImConvUserDTOMap.get(conv.getId()).getUnreadCount());
    //         }
    //     });
    //
    //     return convList;
    // }
    //
    // /**
    //  * 删除会话
    //  * @param convId 会话ID
    //  * @param userId 用户ID
    //  */
    // @Override
    // @Transactional(rollbackFor = Exception.class)
    // public void deleteConv(Long convId, Long userId) {
    //     QueryWrapper<ImConvUser> queryWrapper = new QueryWrapper<>();
    //     queryWrapper.eq("conv_id", convId);
    //     queryWrapper.eq("user_id", userId);
    //
    //     imConvUserService.remove(queryWrapper);
    // }
}
