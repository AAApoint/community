package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
public class PostTests {

    @Resource
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testInsertPost(){
        DiscussPost post = new DiscussPost();
        post.setUserId(101);
        post.setTitle("aa");
        post.setContent("bb");
        post.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(post);
    }

    @Test
    public void testSelectPost(){
        DiscussPost post = discussPostMapper.selectDiscussPostById(284);
        System.out.println(post);
    }


}
