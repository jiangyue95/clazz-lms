package com.jiangyue.service;

import com.jiangyue.pojo.Clazz;
import com.jiangyue.pojo.ClazzQueryParam;
import com.jiangyue.pojo.PageResult;

import java.util.List;

public interface ClazzService {

    PageResult<Clazz> page(ClazzQueryParam clazzQueryParam);

    /**
     * 保存班级信息
     * @param clazz 班级对象
     */
    void save(Clazz clazz);

    /**
     * 根据ID在数据库查询班级信息
     * @param id 传入的 id
     * @return 返回一个 Clazz 对象
     */
    Clazz getClassById(Integer id);

    /**
     * 根据传入 Clazz 对象修改已有的 Clazz 对象
     * @param clazz 传入的 Clazz 对象
     */
    void modifyClazz(Clazz clazz);

    /**
     * 根据传入的 id 删除 clazz
     * @param id 要删除的 clazz id
     */
    void deleteClazzById(Integer id);

    /**
     * 获取全部 Clazz
     * @return
     */
    List<Clazz> getAllClazzs();
}
