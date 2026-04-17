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
     * Get employee basic information by query conditions
     * @param empListQueryDTO query conditions
     * @return a list of employee basic information
     */
    public List<EmpVO> empList(EmpListQueryDTO empListQueryDTO);

    /**
     * Create new employee
     * @param emp an employee entity
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
     * Update employee basic information by id
     * @param emp an employee entity
     */
    void updateById(Emp emp);

    /**
     * Get employee job position data
     * @return a list of employee job position data
     */
    @MapKey("pos")
    List<Map<String, Object>> countEmpJobData();

    /**
     * Get employee gender data
     * @return a list of employee gender data
     */
    @MapKey("name")
    List<Map<String, Object>> countEmpGenderData();

    /**
     * Get all employee basic information
     * @return a list of employee basic information
     */
    List<EmpVO> queryAll();

    /**
     * Get employee basic information by username and password
     * @param emp an employee entity
     * @return an employee entity
     */
    @Select("select id, username, name from emp where username = #{username} and password = #{password}")
    Emp selectByUsernameAndPassword(Emp emp);

    /**
     * Get employee basic information by username
     * @param username username
     * @return an employee entity
     */
    @Mapper
    Emp getByUsername(String username);
}
