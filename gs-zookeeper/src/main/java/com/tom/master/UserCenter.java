package com.tom.master;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserCenter implements Serializable {
    /**
     * 机器ID
     */
    int clientId;
    /**
     * 机器名称
     */
    String name;

}
