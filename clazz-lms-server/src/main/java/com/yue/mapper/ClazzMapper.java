package com.yue.mapper;

import com.yue.pojo.entity.Clazz;
import com.yue.pojo.ClazzQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ClazzMapper {

    /**
     *
     * @return
     */
    public List<Clazz> list(ClazzQueryParam clazzQueryParam);

    /**
     * 在 clazz 表中插入
     * @param clazz
     */
    @Insert("insert into clazz(name, room, begin_date, end_date, master_id, subject, create_time, update_time)" +
            "    values (#{name}, #{room}, #{beginDate}, #{endDate}, #{masterId}, #{subject},  #{createTime}, #{updateTime})")
    void insert(Clazz clazz);

    @Select("SELECT id, name, room, begin_date, end_date, master_id, subject, create_time, update_time From clazz WHERE id = #{id}")
    Clazz selectById(Integer id);

    /**
     * 根据传入的 clazz 对象，更新数据库中已有的 clazz 对象
     * @param clazz clazz 对象
     */
    void modifyClazz(Clazz clazz);

    @Delete("DELETE FROM clazz where id = #{id}")
    void deleteClazzById(Integer id);

    @Select("SELECT id, name, room, begin_date AS beginDate, end_date AS endDate, master_id AS masterId, subject, create_time AS createTime, update_time AS updateTime FROM clazz")
    List<Clazz> getAllClazzs();
}
