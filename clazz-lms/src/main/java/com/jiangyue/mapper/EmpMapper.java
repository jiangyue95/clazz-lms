package com.jiangyue.mapper;

import com.jiangyue.pojo.*;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface EmpMapper {
    // 原始分页查询实现
    /**
     * 查询员工总数
     * @return
     */
//    @Select("select count(*) from emp e left join dept d on e.dept_id = d.id")
//    public Long count();

    /**
     * 进行分页查询的方法
     *
     * @return
     */
//    @Select("""
//            select e.*, d.name as deptName
//            from emp e left join dept d
//            on e.dept_id = d.id
//            order by e.update_time desc
//            limit #{start}, #{pageSize}""")
//    public List<Emp> list(Integer start, Integer pageSize);

//    /**
//     *
//     * @return
//     * 使用 PageHelper 插件 SQL语句不可以加分号
//     * PageHelper 仅仅能对紧跟在其后的第一个查询语句进行分页处理。
//     */
//    @Select("""
//            select e.*, d.name as deptName
//            from emp e left join dept d
//            on e.dept_id = d.id
//            order by e.update_time desc""")
//    public List<Emp> list(String name, Integer gender, LocalDate begin, LocalDate end);

    /**
     *
     * @param empQueryParam
     * @return
     */
    public List<Emp> list(EmpQueryParam empQueryParam);

    @Options(useGeneratedKeys = true, keyProperty = "id") // 重要获取到生成的主键
    @Insert("insert into emp(username, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time)" +
            "    values (#{username}, #{name}, #{gender}, #{phone}, #{job}, #{salary},  #{image}, #{entryDate}, #{deptId}, #{createTime}, #{updateTime})")
    public void insert(Emp emp);

    /**
     * 根据 ID 批量删除员工的基本信息
     * @param ids
     */

    void deletByIds(List<Integer> ids);

    /**
     * 根据 ID 获取员工信息
     * @param id
     * @return
     */

    Emp getById(Integer id);

    /**
     * 根据 ID 更新员工的基本信息
     * @param emp
     */
    void updateById(Emp emp);

    /**
     * 统计员工职位人数
     * @return
     */
    @MapKey("pos")
    List<Map<String, Object>> countEmpJobData();

    @MapKey("name")
    List<Map<String, Object>> countEmpGenderData();


    List<Emp> queryAll();

    @Select("select id, username, name from emp where username = #{username} and password = #{password}")
    Emp selectByUsernameAndPassword(Emp emp);
}
