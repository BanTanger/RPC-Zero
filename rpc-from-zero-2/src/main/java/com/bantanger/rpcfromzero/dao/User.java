package com.bantanger.rpcfromzero.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/9 10:17
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable { // 实现序列化接口，否则会报错
    private Integer id;
    private String userName;
    private Boolean sex;
}
