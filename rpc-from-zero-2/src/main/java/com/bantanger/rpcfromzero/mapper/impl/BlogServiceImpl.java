package com.bantanger.rpcfromzero.mapper.impl;

import com.bantanger.rpcfromzero.dao.Blog;
import com.bantanger.rpcfromzero.mapper.BlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/11 9:05
 */

public class BlogServiceImpl implements BlogService {

    private static final Logger logger = LoggerFactory.getLogger(BlogServiceImpl.class);

    @Override
    public Blog getBlogById(Integer id) {
        Blog blog = Blog.builder().id(id).title("我的博客").userId(22).build();
        logger.info("客户端查询了id = {} 的博客", id);
        return blog;
    }

}
