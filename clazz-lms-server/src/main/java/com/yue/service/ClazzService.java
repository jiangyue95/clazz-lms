package com.yue.service;

import com.yue.pojo.dto.ClazzSaveDTO;
import com.yue.pojo.dto.ClazzUpdateDTO;
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
     * Add new clazz(class) to database
     * @param clazzSaveDTO clazz save dto
     * @return the created clazz with its generated id
     */
    ClazzVO save(ClazzSaveDTO clazzSaveDTO);

    /**
     * Query clazz(class) by id.
     *
     * @param id clazz id
     * @return clazz vo object
     */
    ClazzVO getClassById(Integer id);

    /**
     * Modify clazz(class) identified by URL path id.
     *
     * @param id clazz id (from URL, authoritative)
     * @param clazzUpdateDTO update payload
     * @return the updated clazz
     */
    ClazzVO modifyClazz(Integer id, ClazzUpdateDTO clazzUpdateDTO);

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
