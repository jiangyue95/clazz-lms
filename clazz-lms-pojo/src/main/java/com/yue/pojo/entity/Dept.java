package com.yue.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * department entity
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Dept {

    // primary key
    private Integer id;

    // department name
    private String name;

    // create time
    private LocalDateTime createTime;

    // update time
    private LocalDateTime updateTime;
}
