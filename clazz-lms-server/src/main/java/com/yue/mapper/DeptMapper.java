package com.yue.mapper;

import com.yue.pojo.entity.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * department mapper
 */
@Mapper
public interface DeptMapper {

    /**
     * Query all departments
     * @return all departments list
     */
    @Select("select id, name, create_time, update_time from dept order by update_time desc")
    List<Dept> findAll();

    /**
     * Delete department by id
     * @param id department ID
     */
    @Delete("delete from dept where id = #{id}")
    void deleteById(Integer id);

    /**
     * Add new department
     * @param dept new department
     */
    @Insert("insert into dept (name, create_time, update_time) values (#{name}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Dept dept);

    /**
     * Query department by id
     * @param id department ID
     * @return department
     */
    @Select("select id, name, create_time, update_time from dept where id = #{id}")
    Dept getById(Integer id);

    /**
     * Update department
     * @param dept updated department
     */
    @Update("update dept set name = #{name}, update_time = #{updateTime} where id = #{id}")
    void update(Dept dept);

    /**
     * Query count of employees in department
     * @param id department ID
     * @return count of employees in department
     */
    @Select("select count(*) from emp where dept_id = #{id}")
    Integer countByDeptId(Integer id);
}
