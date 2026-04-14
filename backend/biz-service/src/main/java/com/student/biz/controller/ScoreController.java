package com.student.biz.controller;

import com.student.biz.common.ApiResponse;
import com.student.biz.entity.Score;
import com.student.biz.mapper.ScoreMapper;
import com.student.biz.vo.page.PageResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/biz/scores")
public class ScoreController {
    private final ScoreMapper scoreMapper;

    public ScoreController(ScoreMapper scoreMapper) {
        this.scoreMapper = scoreMapper;
    }

    @GetMapping
    public ApiResponse<List<Score>> list() {
        return ApiResponse.success(scoreMapper.selectList(null));
    }

    @GetMapping("/page")
    public ApiResponse<PageResult<Score>> page(
            @RequestParam(name = "current", defaultValue = "1") Long current,
            @RequestParam(name = "size", defaultValue = "10") Long size) {
        List<Score> all = scoreMapper.selectList(null);
        long total = all.size();
        long start = Math.max((current - 1) * size, 0);
        long end = Math.min(start + size, total);
        List<Score> records = start >= total
                ? Collections.emptyList()
                : all.subList((int) start, (int) end);
        return ApiResponse.success(new PageResult<>(total, current, size, records));
    }

    @PostMapping
    public ApiResponse<Void> save(@RequestBody Score score) {
        scoreMapper.insert(score);
        return ApiResponse.success("保存成功", null);
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody Score score) {
        score.setId(id);
        scoreMapper.updateById(score);
        return ApiResponse.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        scoreMapper.deleteById(id);
        return ApiResponse.success("删除成功", null);
    }
}
