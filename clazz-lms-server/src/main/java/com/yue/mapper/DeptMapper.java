package com.yue.mapper;

import com.yue.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeptMapper {
    /**
     * 查询所有部门数据
     *
     * @return
     */
    //方式一：手动结果映射
//    @Results(
//            {
//                    @Result(column = "creat_time", property = "createTime"),
//                    @Result(column = "update_time", property = "updateTime"),
//            }
//    )
//    @Select("select id, name, dept.create_time, dept.update_time from dept order by update_time desc")
    // 方式二：起别名
//    @Select("select id, name, create_time createTime, update_time updateTime from dept order by update_time desc")
    @Select("select id, name, create_time, update_time from dept order by update_time desc")
    List<Dept> findAll();

    @Delete("delete from dept where id = #{id}")
    void deleteById(Integer id);

    @Insert("insert into dept (name, create_time, update_time) values (#{name}, #{createTime}, #{updateTime})")
    void insert(Dept dept);

    @Select("select id, name, create_time, update_time from dept where id = #{id}")
    Dept getById(Integer id);

    @Update("update dept set name = #{name}, update_time = #{updateTime} where id = #{id}")
    void update(Dept dept);

    @Select("select count(*) from emp where dept_id = #{id}")
    Integer countByDeptId(Integer id);
}
