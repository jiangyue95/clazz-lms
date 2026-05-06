package com.yue.mapper;

import com.yue.pojo.entity.Clazz;
import com.yue.pojo.ClazzQueryParam;
import com.yue.pojo.vo.ClazzVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * clazz(class) table mapper
 */
@Mapper
public interface ClazzMapper {

    /**
     * query clazz list by query param
     * @param clazzQueryParam query param
     * @return clazz list vo
     */
    public List<ClazzVO> list(ClazzQueryParam clazzQueryParam);

    /**
     * insert clazz to clazz table
     * @param clazz clazz object
     */
    @Insert("insert into clazz(name, room, begin_date, end_date, master_id, subject, create_time, update_time)" +
            "    values (#{name}, #{room}, #{beginDate}, #{endDate}, #{masterId}, #{subject},  #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Clazz clazz);

    /**
     * query clazz by id
     * @param id clazz id
     * @return clazz vo object
     */
    @Select("SELECT id, name, room, begin_date, end_date, master_id, subject, create_time, update_time From clazz WHERE id = #{id}")
    ClazzVO selectById(Integer id);

    /**
     * update clazz by id
     * @param clazz clazz object
     */
    void modifyClazz(Clazz clazz);

    /**
     * delete clazz by id
     * @param id clazz id
     */
    @Delete("DELETE FROM clazz where id = #{id}")
    void deleteClazzById(Integer id);

    /**
     * query all clazz list
     * @return clazz list vo
     */
    @Select("SELECT id, name, room, begin_date AS beginDate, end_date AS endDate, master_id AS masterId, subject, create_time AS createTime, update_time AS updateTime FROM clazz")
    List<ClazzVO> getAllClazzs();
}
