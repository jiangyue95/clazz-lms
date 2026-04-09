package com.jiangyue.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Clazz {
    private Integer id;
    private String name;
    private String room;
    private LocalDate beginDate;
    private LocalDate endDate;
    private Integer masterId;
    private Integer subject;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private String masterName;
    private String status;
}
