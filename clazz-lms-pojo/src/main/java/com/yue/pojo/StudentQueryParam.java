package com.yue.pojo;

import lombok.Data;

@Data
public class StudentQueryParam {
    private String name;
    private Integer degree;
    private Integer clazzId;
    private Integer page;
    private Integer pageSize;
}
