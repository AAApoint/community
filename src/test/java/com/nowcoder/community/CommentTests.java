package com.nowcoder.community;

import com.nowcoder.community.dao.CommentMapper;
import com.nowcoder.community.entity.Comment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class CommentTests {

    @Resource
    private CommentMapper commentMapper;

    @Test
    public void testComment(){
        List<Comment> comments = commentMapper.selectCommentsByEntity(1, 228, 0, Integer.MAX_VALUE);

        for(Comment c : comments){
            System.out.println(c);
        }

        System.out.println(commentMapper.selectCountByEntity(1, 228));
    }

    @Test
    public void testInsertComment(){
        Comment comment = new Comment();
        comment.setUserId(111);
        comment.setContent("haha");
        comment.setEntityType(1);
        comment.setEntityId(285);
        comment.setStatus(0);
        comment.setTargetId(0);
        comment.setCreateTime(new Date());
        commentMapper.insertComment(comment);
    }
}
