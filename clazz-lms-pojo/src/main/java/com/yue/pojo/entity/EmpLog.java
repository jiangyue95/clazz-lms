package com.yue.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * employee log entity
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EmpLog {

    // primary key
    private Integer id;

    // operate time
    private LocalDateTime operateTime;

    // detail
    private String info;
}
