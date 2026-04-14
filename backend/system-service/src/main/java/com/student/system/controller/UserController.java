package com.student.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.student.system.common.ApiResponse;
import com.student.system.dto.user.UserCreateRequest;
import com.student.system.dto.user.UserUpdateRequest;
import com.student.system.entity.SysUser;
import com.student.system.mapper.SysUserMapper;
import com.student.system.vo.UserVO;
import com.student.system.vo.page.PageResult;
import jakarta.validation.Valid;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/system/users")
public class UserController {
    private final SysUserMapper userMapper;

    public UserController(SysUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping
    public ApiResponse<List<UserVO>> list() {
        List<UserVO> users = userMapper.selectList(null).stream()
                .map(u -> new UserVO(u.getId(), u.getUsername(), u.getRole()))
                .toList();
        return ApiResponse.success(users);
    }

    @GetMapping("/page")
    public ApiResponse<PageResult<UserVO>> page(
            @RequestParam(name = "current", defaultValue = "1") Long current,
            @RequestParam(name = "size", defaultValue = "10") Long size,
            @RequestParam(name = "username", required = false) String username) {
        LambdaQueryWrapper<SysUser> query = new LambdaQueryWrapper<>();
        query.like(username != null && !username.isBlank(), SysUser::getUsername, username)
                .orderByDesc(SysUser::getId);
        List<SysUser> all = userMapper.selectList(query);
        long total = all.size();
        long start = Math.max((current - 1) * size, 0);
        long end = Math.min(start + size, total);
        List<SysUser> pageList = start >= total
                ? Collections.emptyList()
                : all.subList((int) start, (int) end);
        List<UserVO> records = pageList.stream()
                .map(u -> new UserVO(u.getId(), u.getUsername(), u.getRole()))
                .toList();
        return ApiResponse.success(new PageResult<>(total, current, size, records));
    }

    @PostMapping
    public ApiResponse<Void> create(@RequestBody @Valid UserCreateRequest request) {
        SysUser exists = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, request.getUsername()));
        if (exists != null) {
            return ApiResponse.fail(400, "用户名已存在");
        }

        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(md5(request.getPassword()));
        user.setRole(request.getRole() == null || request.getRole().isBlank() ? "STUDENT" : request.getRole());
        userMapper.insert(user);
        return ApiResponse.success("新增用户成功", null);
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            return ApiResponse.fail(404, "用户不存在");
        }

        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            SysUser exists = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getUsername, request.getUsername())
                    .ne(SysUser::getId, id));
            if (exists != null) {
                return ApiResponse.fail(400, "用户名已存在");
            }
            user.setUsername(request.getUsername());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(md5(request.getPassword()));
        }
        if (request.getRole() != null && !request.getRole().isBlank()) {
            user.setRole(request.getRole());
        }
        userMapper.updateById(user);
        return ApiResponse.success("更新用户成功", null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        int rows = userMapper.deleteById(id);
        if (rows == 0) {
            return ApiResponse.fail(404, "用户不存在");
        }
        return ApiResponse.success("删除用户成功", null);
    }

    private String md5(String plain) {
        return DigestUtils.md5DigestAsHex(plain.getBytes(StandardCharsets.UTF_8));
    }
}
