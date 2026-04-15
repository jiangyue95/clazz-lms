package com.yue.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yue.exception.ClazzHasStudentException;
import com.yue.mapper.ClazzMapper;
import com.yue.mapper.StudentMapper;
import com.yue.pojo.entity.Clazz;
import com.yue.pojo.ClazzQueryParam;

import com.yue.pojo.PageResult;
import com.yue.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {

    @Autowired
    private ClazzMapper clazzMapper;
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public PageResult<Clazz> page(ClazzQueryParam clazzQueryParam) {
        // 1. 设置分页参数（PageHelper）
        PageHelper.startPage(clazzQueryParam.getPage(), clazzQueryParam.getPageSize());

        // 2. 执行查询
        List<Clazz> clazzList = clazzMapper.list(clazzQueryParam);

        // 3. 计算课程状态
        LocalDate now = LocalDate.now();
        for (Clazz clazz : clazzList) {
            if (clazz.getBeginDate() != null && clazz.getEndDate() != null) {
                if (now.isBefore(clazz.getBeginDate())) {
                    clazz.setStatus("未开班");
                } else if (now.isAfter(clazz.getEndDate())) {
                    clazz.setStatus("已结课");
                } else {
                    clazz.setStatus("已开班");
                }
            } else {
                clazz.setStatus("未知"); // 或者不做处理
            }
        }

        // 4. 封装结果
        Page<Clazz> p = (Page<Clazz>) clazzList;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    @Override
    public void save(Clazz clazz) {
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.insert(clazz);
    }

    @Override
    public Clazz getClassById(Integer id) {
        Clazz clazz = clazzMapper.selectById(id);
        return clazz;
    }

    @Override
    public void modifyClazz(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.modifyClazz(clazz);
    }

    @Override
    @Transactional
    public void deleteClazzById(Integer id) {
        // 查询班级人数
        Integer studentCount = studentMapper.countByClazzId(id);
        if (studentCount > 0) {
            throw new ClazzHasStudentException("对不起, 该班级下有学生, 不能直接删除");
        }
        clazzMapper.deleteClazzById(id);
    }

    @Override
    public List<Clazz> getAllClazzs() {
        List<Clazz> clazzList = clazzMapper.getAllClazzs();
        return clazzList;
    }
}
