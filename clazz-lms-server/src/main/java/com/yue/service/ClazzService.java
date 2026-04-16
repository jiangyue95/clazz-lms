package com.yue.service;

import com.yue.pojo.dto.ClazzSaveDTO;
import com.yue.pojo.dto.ClazzUpdateDTO;
import com.yue.pojo.entity.Clazz;
import com.yue.pojo.ClazzQueryParam;
import com.yue.pojo.PageResult;
import com.yue.pojo.vo.ClazzVO;

import java.util.List;

/**
 * clazz(class) service interface
 */
public interface ClazzService {

    PageResult<ClazzVO> page(ClazzQueryParam clazzQueryParam);

    /**
     * Add new class to database
     * @param clazzSaveDTO clazz save dto
     */
    void save(ClazzSaveDTO clazzSaveDTO);

    /**
     * query clazz by id
     * @param id clazz id
     * @return clazz vo object
     */
    ClazzVO getClassById(Integer id);

    /**
     * modify clazz by id and update dto
     * @param clazzUpdateDTO clazz update dto
     */
    void modifyClazz(ClazzUpdateDTO clazzUpdateDTO);

    /**
     * delete clazz by id
     * @param id clazz id
     */
    void deleteClazzById(Integer id);

    /**
     * query all clazz
     * @return clazz vo list object
     */
    List<ClazzVO> getAllClazzs();
}
