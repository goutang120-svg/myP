package com.student.biz.controller;

import com.student.biz.common.ApiResponse;
import com.student.biz.entity.Student;
import com.student.biz.mapper.StudentMapper;
import com.student.biz.vo.page.PageResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/biz/students")
public class StudentController {
    private final StudentMapper studentMapper;

    public StudentController(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    @GetMapping
    public ApiResponse<List<Student>> list() {
        return ApiResponse.success(studentMapper.selectList(null));
    }

    @GetMapping("/page")
    public ApiResponse<PageResult<Student>> page(
            @RequestParam(name = "current", defaultValue = "1") Long current,
            @RequestParam(name = "size", defaultValue = "10") Long size) {
        List<Student> all = studentMapper.selectList(null);
        long total = all.size();
        long start = Math.max((current - 1) * size, 0);
        long end = Math.min(start + size, total);
        List<Student> records = start >= total
                ? Collections.emptyList()
                : all.subList((int) start, (int) end);
        return ApiResponse.success(new PageResult<>(total, current, size, records));
    }

    @PostMapping
    public ApiResponse<Void> save(@RequestBody Student student) {
        studentMapper.insert(student);
        return ApiResponse.success("保存成功", null);
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody Student student) {
        student.setId(id);
        studentMapper.updateById(student);
        return ApiResponse.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        studentMapper.deleteById(id);
        return ApiResponse.success("删除成功", null);
    }
}
