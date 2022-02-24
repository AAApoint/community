package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/conversation/list", method = RequestMethod.GET)
    public String getConversationList(Model model, Page page){
        User user = hostHolder.getUser();

        page.setLimit(5);
        page.setPath("/conversation/list");
        page.setRows(messageService.getConversationCount(user.getId()));

        List<Message> conversationList = messageService.getConversationList(user.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> conversations = new ArrayList<>();
        if(conversationList != null){
            for(Message message : conversationList){
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", message);
                map.put("letterCount", messageService.getLetterCount(message.getConversationId()));
                map.put("unreadNum", messageService.getUnreadNum(user.getId(), message.getConversationId()));

                int targetId = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
                map.put("target", userService.findUserById(targetId));

                conversations.add(map);
            }
        }

        model.addAttribute("conversations", conversations);
        int totalUnreadNum = messageService.getUnreadNum(user.getId(), null);
        model.addAttribute("totalUnreadNum", totalUnreadNum);

        return "/site/letter";
    }

    @RequestMapping(path = "/conversation/detail/{conversationId}", method = RequestMethod.GET)
    public String getConversationDetail(@PathVariable("conversationId") String conversationId, Model model, Page page){
        // 分页信息
        page.setLimit(5);
        page.setPath("/conversation/detail/" + conversationId);
        page.setRows(messageService.getLetterCount(conversationId));

        List<Message> letterList = messageService.getLetterList(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> letters = new ArrayList<>();
        if(letterList != null){
            for(Message message : letterList){
                Map<String, Object> map = new HashMap<>();
                map.put("letter", message);
                map.put("user", userService.findUserById(message.getFromId()));
                letters.add(map);
            }
        }

        model.addAttribute("letters", letters);
        model.addAttribute("targetUser", getConversationTarget(conversationId));

        List<Integer> ids = getLettersId(letterList);
        if(!ids.isEmpty()){
            messageService.readMessage(ids, 1);
        }

        return "/site/letter-detail";
    }

    private List<Integer> getLettersId(List<Message> letterList){
        List<Integer> unreadList = new ArrayList<>();

        if(letterList != null){
            for(Message message : letterList){
                if(hostHolder.getUser().getId() == message.getToId() && message.getStatus() == 0){
                    unreadList.add(message.getId());
                }
            }
        }

        return unreadList;
    }

    private User getConversationTarget(String conversationId){
        String[] userIds = conversationId.split("_");
        int id1 = Integer.parseInt(userIds[0]);
        int id2 = Integer.parseInt(userIds[1]);
        return hostHolder.getUser().getId() == id1 ? userService.findUserById(id2) : userService.findUserById(id1);
    }

    @RequestMapping(path = "/conversation/send", method = RequestMethod.POST)
    @ResponseBody
    public String sendMessage(String toName, String content){
        User target = userService.findUserByName(toName);
        if(target == null){
            return CommunityUtil.getJSONString(1, "该用户不存在!");
        }

        Message message = new Message();
        message.setFromId(hostHolder.getUser().getId());
        message.setToId(target.getId());
        message.setStatus(0);
        message.setContent(content);
        message.setCreateTime(new Date());
        if(message.getFromId() < message.getToId()){
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        }else{
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        messageService.addMessage(message);

        return CommunityUtil.getJSONString(0);
    }
}
