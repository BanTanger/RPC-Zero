package com.bantanger.rpcfromzero.util;

import lombok.AllArgsConstructor;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/18 20:58
 */

@AllArgsConstructor
public enum MessageType {
   REQUEST(0), RESPONSE(1);
   private int code;
   public int getCode() {
       return code;
   }
}
