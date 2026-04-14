package com.student.biz.controller;

import com.student.biz.common.ApiResponse;
import com.student.biz.entity.Course;
import com.student.biz.mapper.CourseMapper;
import com.student.biz.vo.page.PageResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/biz/courses")
public class CourseController {
    private final CourseMapper courseMapper;

    public CourseController(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    @GetMapping
    public ApiResponse<List<Course>> list() {
        return ApiResponse.success(courseMapper.selectList(null));
    }

    @GetMapping("/page")
    public ApiResponse<PageResult<Course>> page(
            @RequestParam(name = "current", defaultValue = "1") Long current,
            @RequestParam(name = "size", defaultValue = "10") Long size) {
        List<Course> all = courseMapper.selectList(null);
        long total = all.size();
        long start = Math.max((current - 1) * size, 0);
        long end = Math.min(start + size, total);
        List<Course> records = start >= total
                ? Collections.emptyList()
                : all.subList((int) start, (int) end);
        return ApiResponse.success(new PageResult<>(total, current, size, records));
    }

    @PostMapping
    public ApiResponse<Void> save(@RequestBody Course course) {
        courseMapper.insert(course);
        return ApiResponse.success("保存成功", null);
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody Course course) {
        course.setId(id);
        courseMapper.updateById(course);
        return ApiResponse.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        courseMapper.deleteById(id);
        return ApiResponse.success("删除成功", null);
    }
}
