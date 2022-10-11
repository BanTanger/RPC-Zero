package com.bantanger.rpcfromzero.mapper.impl;

import com.bantanger.rpcfromzero.dao.User;
import com.bantanger.rpcfromzero.mapper.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/9 10:22
 */
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User getUserByUserId(Integer id) {
        logger.info("客户端模拟查询了id为 {} 的用户", id);
        // 模拟雪花算法。从数据库中取出用户
        Random random = new Random();
        User user = User.builder()
                .userName(UUID.randomUUID().toString())
                .sex(random.nextBoolean()).id(id)
                .build();
        return user;
    }

    @Override
    public int insertUser(User user) {
        // 模拟插入数据
        logger.info("插入数据成功 {}", user);
        return 1;
    }

}
