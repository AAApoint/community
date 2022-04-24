package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {

    @Resource
    private UserMapper userMapper;

    @Resource
    private DiscussPostMapper discussPostMapper;

    @Resource
    private LoginTicketMapper loginTicketMapper;

    @Resource
    private MessageMapper messageMapper;

    @Test
    public void testSelectUser(){
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("liubei");
        System.out.println(user);

        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }

    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("http://www.baidu.com");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void updateUser(){
        int rows = userMapper.updateStatus(150, 1);
        System.out.println(rows);

        rows = userMapper.updateHeader(150, "http://www.baiduuuu.com");
        System.out.println(rows);

        rows = userMapper.updatePassword(150, "hhhhello");
        System.out.println(rows);
    }

//    @Test
//    public void selectDiscussPost(){
//        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(0, 0, 10);
//        for(DiscussPost discussPost : discussPosts){
//            System.out.println(discussPost);
//        }
//
//        int rows = discussPostMapper.selectDiscussPostRows(0);
//        System.out.println(rows);
//    }

    @Test
    public void insertTicket(){
        LoginTicket lt = new LoginTicket();
        lt.setUserId(150);
        lt.setTicket("abc");
        lt.setStatus(0);
        lt.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
        loginTicketMapper.insertLoginTicket(lt);
    }

    @Test
    public void updateTicket(){
        LoginTicket lt = loginTicketMapper.selectByTicket("abc");
        System.out.println(lt);

        loginTicketMapper.updateStatus(lt.getTicket(), 0);
        lt = loginTicketMapper.selectByTicket("abc");
        System.out.println(lt);
    }

    @Test
    public void testMessage(){
        List<Message> list = messageMapper.selectConversationList(111, 0, 20);
        for(Message m: list){
            System.out.println(m);
        }

        System.out.println(messageMapper.selectConversationCount(111));

        List<Message> list2 = messageMapper.selectLetterList("111_112", 0, 20);
        for(Message m : list2){
            System.out.println(m);
        }

        System.out.println(messageMapper.selectLetterCount("111_112"));

        System.out.println(messageMapper.selectUnreadNum(131, "111_131"));


    }
}
