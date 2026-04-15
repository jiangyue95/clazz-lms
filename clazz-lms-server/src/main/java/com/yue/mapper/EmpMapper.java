package com.yue.mapper;

import com.yue.pojo.dto.EmpListQueryDTO;
import com.yue.pojo.entity.Emp;
import com.yue.pojo.vo.EmpVO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * Employee mapper
 */
@Mapper
public interface EmpMapper {

    /**
     *
     * @param
     * @return
     */
    public List<EmpVO> empList(EmpListQueryDTO empListQueryDTO);

    /**
     * Create new employee
     * @param emp
     */
    @Options(useGeneratedKeys = true, keyProperty = "id") // 重要获取到生成的主键
    @Insert("insert into emp(username, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time)" +
            "    values (#{username}, #{name}, #{gender}, #{phone}, #{job}, #{salary},  #{image}, #{entryDate}, #{deptId}, #{createTime}, #{updateTime})")
    public void insert(Emp emp);

    /**
     * Batch delete employee basic information by a list of id
     * @param ids a list of employee id
     */
    void deleteByIds(List<Integer> ids);

    /**
     * Get employee basic information by id
     * @param id employee id
     * @return an employee entity
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

    // Select employee by username
    @Mapper
    Emp getByUsername(String username);
}
