package com.student.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.student.system.common.ApiResponse;
import com.student.system.dto.AuthResponse;
import com.student.system.dto.LoginRequest;
import com.student.system.dto.RegisterRequest;
import com.student.system.entity.SysUser;
import com.student.system.mapper.SysUserMapper;
import com.student.system.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/system/auth")
public class AuthController {
    private final SysUserMapper userMapper;
    private final JwtUtil jwtUtil;

    public AuthController(SysUserMapper userMapper, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestBody @Valid RegisterRequest request) {
        SysUser existsByUsername = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, request.getUsername()));
        if (existsByUsername != null) {
            return ApiResponse.fail(400, "用户名已存在");
        }
        SysUser existsByPhone = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhone, request.getPhone()));
        if (existsByPhone != null) {
            return ApiResponse.fail(400, "手机号已存在");
        }
        SysUser existsByEmail = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getEmail, request.getEmail()));
        if (existsByEmail != null) {
            return ApiResponse.fail(400, "邮箱已存在");
        }
        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(md5(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setRole("STUDENT");
        userMapper.insert(user);
        return ApiResponse.success("注册成功", null);
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, request.getUsername()));
        if (user == null || !user.getPassword().equals(md5(request.getPassword()))) {
            return ApiResponse.fail(400, "用户名或密码错误");
        }
        return ApiResponse.success(new AuthResponse(jwtUtil.createToken(user.getUsername()), user.getUsername()));
    }

    private String md5(String plain) {
        return DigestUtils.md5DigestAsHex(plain.getBytes(StandardCharsets.UTF_8));
    }
}
