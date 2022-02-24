package com.nowcoder.community.util;

public interface CommunityConstant {

    // 激活成功
    int ACTIVATION_SUCCESS = 0;

    // 重复激活
    int ACTIVATION_REPEAT = 1;

    // 激活失败
    int ACTIVATION_FAILURE = 2;

    // 登陆凭证默认记忆时间
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    // 记住状态的登陆凭证记忆时间
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;

    // 帖子的评论类型
    int ENTITY_TYPE_POST = 1;

    // 评论的评论类型
    int ENTITY_TYPE_COMMENT = 2;

    // 用戶的类型
    int ENTITY_TYPE_USER = 3;
}
