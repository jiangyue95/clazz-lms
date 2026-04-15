package com.yue.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Class size statistics vo (view object)
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ClazzCountVO {

    // clazz(class) list
    private List clazzList;

    // class size list
    private List dataList;
}
