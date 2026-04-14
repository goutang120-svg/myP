<template>
  <div class="login-page">
    <div class="login-bg-shape login-bg-shape-1"></div>
    <div class="login-bg-shape login-bg-shape-2"></div>
    <div class="login-grid"></div>

    <el-card class="login-card">
      <div class="login-title-wrap">
        <div class="logo-badge">SM</div>
        <h2 class="login-title">学生管理系统</h2>
        <p class="login-subtitle">Student Management Platform</p>
      </div>
      <el-form :model="form" label-width="0" class="login-form">
        <el-form-item>
          <div class="field-label">用户名</div>
          <el-input v-model="form.username" placeholder="请输入用户名" size="large" />
        </el-form-item>
        <el-form-item>
          <div class="field-label">密码</div>
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" size="large" />
        </el-form-item>
        <el-form-item>
          <div class="login-actions">
            <el-button type="primary" class="btn-login" @click="login">登录</el-button>
            <el-button class="btn-register" @click="register">注册</el-button>
          </div>
        </el-form-item>
      </el-form>
      <div class="login-footer-tip">首次使用可先注册账号</div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import http from '../api/http'

const router = useRouter()
const form = reactive({ username: '', password: '' })

const login = async () => {
  const { data } = await http.post('/system/auth/login', form)
  if (data.code === 200 && data.data) {
    localStorage.setItem('token', data.data.token)
    localStorage.setItem('username', data.data.username)
    ElMessage.success(data.msg || '登录成功')
    await router.push('/dashboard')
  } else {
    ElMessage.error(data.msg || '登录失败')
  }
}

const register = async () => {
  const { data } = await http.post('/system/auth/register', form)
  if (data.code === 200) {
    ElMessage.success(data.msg || '注册成功')
  } else {
    ElMessage.error(data.msg || '注册失败')
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #e8eeff 0%, #dbeafe 35%, #d1fae5 100%);
}

.login-grid {
  position: absolute;
  inset: 0;
  background-image: linear-gradient(rgba(148, 163, 184, 0.12) 1px, transparent 1px),
    linear-gradient(90deg, rgba(148, 163, 184, 0.12) 1px, transparent 1px);
  background-size: 36px 36px;
  mask-image: radial-gradient(circle at center, black 35%, transparent 90%);
}

.login-bg-shape {
  position: absolute;
  border-radius: 999px;
  filter: blur(2px);
}

.login-bg-shape-1 {
  width: 320px;
  height: 320px;
  background: rgba(59, 130, 246, 0.2);
  top: -80px;
  left: -80px;
}

.login-bg-shape-2 {
  width: 280px;
  height: 280px;
  background: rgba(16, 185, 129, 0.2);
  right: -60px;
  bottom: -60px;
}

.login-card {
  width: 430px;
  border-radius: 18px;
  border: 1px solid rgba(255, 255, 255, 0.55);
  background: rgba(255, 255, 255, 0.72);
  backdrop-filter: blur(8px);
  box-shadow: 0 24px 55px rgba(30, 41, 59, 0.18);
  z-index: 1;
}

.login-title-wrap {
  text-align: center;
  margin-bottom: 14px;
}

.logo-badge {
  margin: 2px auto 10px;
  width: 44px;
  height: 44px;
  line-height: 44px;
  border-radius: 12px;
  font-weight: 800;
  color: #fff;
  background: linear-gradient(135deg, #3b82f6, #6366f1);
  box-shadow: 0 8px 16px rgba(59, 130, 246, 0.35);
}

.login-title {
  margin: 0;
  font-size: 26px;
  letter-spacing: 1px;
  color: #111827;
}

.login-subtitle {
  margin: 6px 0 0;
  color: #64748b;
  font-size: 12px;
}

.login-form :deep(.el-input__wrapper) {
  border-radius: 10px;
  box-shadow: 0 0 0 1px #e2e8f0 inset;
}

.login-form :deep(.el-form-item) {
  margin-bottom: 18px;
}

.field-label {
  font-size: 13px;
  color: #475569;
  margin-bottom: 6px;
  font-weight: 600;
}

.login-actions {
  width: 100%;
  display: flex;
  gap: 10px;
}

.login-actions .el-button {
  flex: 1;
  height: 42px;
  border-radius: 10px;
}

.btn-login {
  border: none;
  background: linear-gradient(135deg, #2563eb, #4f46e5);
}

.btn-register {
  border-color: #cbd5e1;
  color: #334155;
}

.login-footer-tip {
  text-align: center;
  margin-top: 2px;
  font-size: 12px;
  color: #64748b;
}
</style>
