package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {

    // 查询所有会话列表
    List<Message> selectConversationList(int userId, int offset, int limit);

    // 查询所有会话数量
    int selectConversationCount(int userId);

    // 查询会话所有私信
    List<Message> selectLetterList(String conversationId, int offset, int limit);

    // 查询会话私信数量
    int selectLetterCount(String conversationId);

    // 查询未读信息数量
    int selectUnreadNum(int userId, String conversationId);

    // 发送私信
    int insertMessage(Message message);

    // 更改私信状态
    int updateMessageStatus(List<Integer> ids, int status);

    // 查询某个主题下最新的通知
    Message selectLatestNotice(int userId, String topic);

    // 查询某个主题所包含的通知数量
    int selectNoticeCount(int userId, String topic);

    // 查询未读的通知数量
    int selectNoticeUnreadCount(int userId, String topic);

    // 查询某个主题的系统消息详情列表
    List<Message> selectNoticeList(int userId, String topic, int offset, int limit);
}
