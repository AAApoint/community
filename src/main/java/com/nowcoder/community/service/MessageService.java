package com.nowcoder.community.service;

import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MessageService {

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private SensitiveFilter sensitiveFilter;

    public List<Message> getConversationList(int userId, int offset, int limit){
        return messageMapper.selectConversationList(userId, offset, limit);
    }

    public int getConversationCount(int userId){
        return messageMapper.selectConversationCount(userId);
    }

    public List<Message> getLetterList(String conversationId, int offset, int limit){
        return messageMapper.selectLetterList(conversationId, offset, limit);
    }

    public int getLetterCount(String conversationId){
        return messageMapper.selectLetterCount(conversationId);
    }

    public int getUnreadNum(int userId, String conversationId){
        return messageMapper.selectUnreadNum(userId, conversationId);
    }

    public int addMessage(Message message){
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    public int readMessage(List<Integer> ids, int status){
        return messageMapper.updateMessageStatus(ids,status);
    }
}
