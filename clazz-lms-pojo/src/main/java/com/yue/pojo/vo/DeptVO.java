package com.yue.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * View Object for Dept(department) - the data representation returned to the client.
 * <p>
 *     Although currently mirrors {@link com.yue.pojo.entity.Dept}, this class exists
 *     to decouple the API contract from the database schema. Future changes to the
 *     Entity (e.g., adding internal fields like `deleted` or `version`) will not
 *     leak to the API consumers.
 * </p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeptVO {

    private Integer id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
