package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController implements CommunityConstant {
    @Resource
    private DiscussPostService discussPostService;

    @Resource
    private LikeService likeService;

    @Resource
    private UserService userService;

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page){
        page.setPath("/index");
        page.setRows(discussPostService.findDiscussPostRows(0));

        List<DiscussPost> discussPosts = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> postList = new ArrayList<>();

        if(discussPosts != null){
            for(DiscussPost discussPost : discussPosts){
                Map<String, Object> map = new HashMap<>();
                map.put("discussPost", discussPost);
                User user = userService.findUserById(discussPost.getUserId());
                map.put("user", user);

                long likeNum = likeService.getLikeNum(ENTITY_TYPE_POST, discussPost.getId());
                map.put("likeNum", likeNum);
                postList.add(map);
            }
        }
        model.addAttribute("postList", postList);
        return "index";
    }

    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String getErrorPage(){
        return "/error/500";
    }
}
