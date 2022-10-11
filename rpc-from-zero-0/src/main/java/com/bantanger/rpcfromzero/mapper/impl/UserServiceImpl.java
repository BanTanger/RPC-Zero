package com.bantanger.rpcfromzero.mapper.impl;

import com.bantanger.rpcfromzero.dao.User;
import com.bantanger.rpcfromzero.mapper.UserService;

import java.util.Random;
import java.util.UUID;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/9 10:22
 */
public class UserServiceImpl implements UserService {

    @Override
    public User getUserByUserId(Integer id) {
        System.out.println("客户端模拟查询了id为 " + id +  " 的用户");
        // 模拟雪花算法。从数据库中取出用户
        Random random = new Random();
        User user = User.builder()
                .userName(UUID.randomUUID().toString())
                .sex(random.nextBoolean()).id(id)
                .build();
        return user;
    }

}
