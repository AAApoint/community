package com.nowcoder.community.util;

public class RedisKeyUtil {

    private static final String SPLIT = ":";

    private static final String PREFIX_ENTITY_LIKE = "like:entity";

    private static final String PREFIX_LIKE_USER = "like:user";

    private static final String PREFIX_FOLLOWEE = "followee";

    private static final String PREFIX_FOLLOWER = "follower";

    public static String getEntityLikeKey(int entityType, int entityId){
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getLikeUserKey(int userId){
        return PREFIX_LIKE_USER + SPLIT + userId;
    }

    // 某用户关注的实体
    // followee:userId:entityType -> zset(entityId,now)
    public static String getFolloweeKey(int userId, int entityType){
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    // 某个实体拥有的粉丝
    // follower:entityType:entityId -> zset(userId,now)
    public static String getFollowerKey(int entityType, int entityId){
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }
}