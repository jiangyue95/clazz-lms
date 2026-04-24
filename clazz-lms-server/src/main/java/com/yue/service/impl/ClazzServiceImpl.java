package com.yue.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yue.exception.BusinessRuleViolationException;
import com.yue.exception.ResourceNotFoundException;
import com.yue.mapper.ClazzMapper;
import com.yue.mapper.StudentMapper;
import com.yue.pojo.dto.ClazzSaveDTO;
import com.yue.pojo.dto.ClazzUpdateDTO;
import com.yue.pojo.entity.Clazz;
import com.yue.pojo.ClazzQueryParam;

import com.yue.pojo.PageResult;
import com.yue.pojo.vo.ClazzVO;
import com.yue.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * clazz(class) service implementation
 */
@Service
public class ClazzServiceImpl implements ClazzService {

    @Autowired
    private ClazzMapper clazzMapper;
    @Autowired
    private StudentMapper studentMapper;

    /**
     * query clazz by query param
     * @param clazzQueryParam query param
     * @return
     */
    @Override
    public PageResult<ClazzVO> page(ClazzQueryParam clazzQueryParam) {
        // 1. Set pagination parameter for PageHelper
        PageHelper.startPage(clazzQueryParam.getPage(), clazzQueryParam.getPageSize());

        // 2. query clazz by query param
        List<ClazzVO> clazzList = clazzMapper.list(clazzQueryParam);

        // 3. calculate class status
        LocalDate now = LocalDate.now();
        for (ClazzVO clazzVO : clazzList) {
            if (clazzVO.getBeginDate() != null && clazzVO.getEndDate() != null) {
                if (now.isBefore(clazzVO.getBeginDate())) {
                    clazzVO.setStatus("Classes not yet started");
                } else if (now.isAfter(clazzVO.getEndDate())) {
                    clazzVO.setStatus("Course completed");
                } else {
                    clazzVO.setStatus("Classes have already started");
                }
            } else {
                clazzVO.setStatus("Other");
            }
        }

        // 4. return page result
        Page<ClazzVO> p = (Page<ClazzVO>) clazzList;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    /**
     * Add new class to database
     * @param clazzSaveDTO clazz save dto
     */
    @Override
    public void save(ClazzSaveDTO clazzSaveDTO) {
        Clazz clazz = Clazz.builder()
                .name(clazzSaveDTO.getName())
                .room(clazzSaveDTO.getRoom())
                .beginDate(clazzSaveDTO.getBeginDate())
                .endDate(clazzSaveDTO.getEndDate())
                .masterId(clazzSaveDTO.getMasterId())
                .subject(clazzSaveDTO.getSubject())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        clazzMapper.insert(clazz);
    }

    /**
     * query clazz by id
     * @param id clazz id
     * @return clazz
     */
    @Override
    public ClazzVO getClassById(Integer id) {
        ClazzVO clazzVO = clazzMapper.selectById(id);
        if (clazzVO == null) {
            throw new ResourceNotFoundException("Clazz(Class) with id " + id + " not found");
        }
        return clazzVO;
    }

    /**
     * update clazz by id
     * @param clazzUpdateDTO clazz update dto
     */
    @Override
    public void modifyClazz(ClazzUpdateDTO clazzUpdateDTO) {
        Clazz clazz = Clazz.builder()
                .id(clazzUpdateDTO.getId())
                .name(clazzUpdateDTO.getName())
                .room(clazzUpdateDTO.getRoom())
                .beginDate(clazzUpdateDTO.getBeginDate())
                .endDate(clazzUpdateDTO.getEndDate())
                .masterId(clazzUpdateDTO.getMasterId())
                .subject(clazzUpdateDTO.getSubject())
                .updateTime(LocalDateTime.now())
                .build();
        clazzMapper.modifyClazz(clazz);
    }

    @Override
    @Transactional
    public void deleteClazzById(Integer id) {
        // Query the student count of clazz(class)
        Integer studentCount = studentMapper.countByClazzId(id);
        if (studentCount > 0) {
            throw new BusinessRuleViolationException("Cannot delete clazz: it still has students enrolled");
        }
        clazzMapper.deleteClazzById(id);
    }

    @Override
    public List<ClazzVO> getAllClazzs() {
        List<ClazzVO> clazzList = clazzMapper.getAllClazzs();
        return clazzList;
    }
}
