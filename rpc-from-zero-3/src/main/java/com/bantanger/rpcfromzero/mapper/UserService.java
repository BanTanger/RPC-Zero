package com.bantanger.rpcfromzero.mapper;

import com.bantanger.rpcfromzero.dao.User;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/9 10:21
 */
public interface UserService {

    User getUserByUserId(Integer id);

    // 新增插入元素接口
    int insertUser(User user);

}
