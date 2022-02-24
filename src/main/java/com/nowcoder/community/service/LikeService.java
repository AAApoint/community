package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    // 点赞
    public void like(int userId, int entityType, int entityId, int entityUserId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String likeUserKey = RedisKeyUtil.getLikeUserKey(entityUserId);


                boolean isMember = operations.opsForSet().isMember(entityLikeKey, userId);
                redisTemplate.multi();
                if(isMember){
                    operations.opsForSet().remove(entityLikeKey, userId);
                    operations.opsForValue().decrement(likeUserKey);
                }else{
                    operations.opsForSet().add(entityLikeKey, userId);
                    operations.opsForValue().increment(likeUserKey);
                }
                return redisTemplate.exec();
            }
        });
    }

    // 统计点赞数量
    public long getLikeNum(int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    // 获得点赞状态
    public int getLikeStatus(int userId, int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }

    // 得到用户获得的点赞总数
    public int findLikeUserCount(int userId){
        String likeUserKey = RedisKeyUtil.getLikeUserKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(likeUserKey);
        return count == null ? 0 : count.intValue();
    }
}
