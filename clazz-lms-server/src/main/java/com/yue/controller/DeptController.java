package com.yue.controller;

import com.yue.anno.Log;
import com.yue.pojo.Dept;
import com.yue.pojo.Result;
import com.yue.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/depts")
@RestController
public class DeptController {

//    private static final Logger log = LoggerFactory.getLogger(DeptController.class); // 固定的

    @Autowired
    private DeptService deptService;

//    @RequestMapping(value = "/depts", method = RequestMethod.GET)
    @GetMapping
    public Result list() {
//        System.out.println("查询全部部门数据");
        log.info("查询全部部门数据");
        List<Dept> deptList = deptService.findAll();
        return Result.success(deptList);
    }

    // 方法一：通过原始方式获得 ID
//    @DeleteMapping("/depts") //限定仅处理 DELETE 方法的请求
//    public Result delete(HttpServletRequest request) {
//        String idStr = request.getParameter("id"); // 获取的参数，默认是 String 类型的
//        int id = Integer.parseInt(idStr); // 转换成 Integer 类型
//        System.out.println("根据 ID 删除部门：" + id);
//        return Result.success();
//    }

    /**
     * 方法二：通过 @RequestParam 注解获得 ID
     * 注意事项：一旦声明了 @RequestParam，该参数在请求时必须传递。
     * 如果不传递将会报错。（默认 required 为 true）
     */
//    @DeleteMapping("/depts")
//    public Result delete(@RequestParam(value = "id", required = false) Integer deptId) {
//        System.out.println("根据 ID 删除部门：" + deptId);
//        return Result.success();
//    }

    /**
     * 删除部门 方式三：省略 @RequestParam （如果请求参数名与形参变量名相同，直接定义方法形参即可接收）（推荐）
     * @param id
     * @return
     */
    @Log
    @DeleteMapping()
    public Result delete(Integer id) {
        log.info("根据 ID 删除部门：{}", id);
        deptService.deleteById(id);
        return Result.success();
    }

    // Controller 类中的响应处理方法
    /**
     * 新增部门
     * @param dept 接收请求体中的 dept 的对象
     * @return
     */
    @Log
    @PostMapping
    public Result add(@RequestBody Dept dept) {
//        System.out.println("添加部门：" + dept);
        log.info("添加部门：{}", dept);
        deptService.add(dept);
        return Result.success();
    }

    /**
     * 根据 id 查询部门
     * @param id 部门 ID
     * @return
     */
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        log.info("根据 ID 查询部门：{}", id);
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }

    /**
     * 修改部门
     * @param dept 修改的部门对象
     * @return 修改成功信息
     */
    @Log
    @PutMapping
    public Result update(@RequestBody Dept dept) {
        log.info("修改部门：{}", dept);
        deptService.update(dept);
        return Result.success();
    }

    /**
     * 根据 id 删除部门
     * @param id 部门 ID
     * @return 删除成功信息
     */
    @Log
    @DeleteMapping("/{id}")
    public Result deleteDeptById(@PathVariable Integer id) {
        log.info("根据 ID 删除部门：{}", id);
        deptService.deleteById(id);
        return Result.success();
    }
}
