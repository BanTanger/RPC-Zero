package com.bantanger.rpcfromzero.mapper.impl;

import com.bantanger.rpcfromzero.dao.Blog;
import com.bantanger.rpcfromzero.mapper.BlogService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/11 9:05
 */

@Slf4j
public class BlogServiceImpl implements BlogService {

    @Override
    public Blog getBlogById(Integer id) {
        Blog blog = Blog.builder().id(id).title("我的博客").userId(22).build();
        log.info("客户端查询了id = {} 的博客", id);
        return blog;
    }

}
