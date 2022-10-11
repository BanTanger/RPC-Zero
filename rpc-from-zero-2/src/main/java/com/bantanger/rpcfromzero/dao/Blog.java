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
 * @Date 2022/10/11 9:04
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Blog implements Serializable {
    private Integer id;
    private Integer userId;
    private String title;
}
