<template>
  <div class="login-page">
    <div class="login-bg-shape login-bg-shape-1"></div>
    <div class="login-bg-shape login-bg-shape-2"></div>
    <div class="login-grid"></div>

    <el-card class="login-card">
      <div class="login-title-wrap">
        <div class="logo-badge">SM</div>
        <h2 class="login-title">创建账号</h2>
        <p class="login-subtitle">注册后即可登录使用</p>
      </div>

      <el-form ref="formRef" :model="form" :rules="rules" label-width="0" class="login-form">
        <el-form-item prop="username">
          <div class="field-label">用户名</div>
          <el-input v-model="form.username" placeholder="请输入用户名" size="large" />
        </el-form-item>

        <el-form-item prop="phone">
          <div class="field-label">手机号</div>
          <el-input v-model="form.phone" placeholder="请输入手机号" size="large" maxlength="11" />
        </el-form-item>

        <el-form-item prop="email">
          <div class="field-label">邮箱</div>
          <el-input v-model="form.email" placeholder="请输入邮箱" size="large" />
        </el-form-item>

        <el-form-item prop="password">
          <div class="field-label">密码</div>
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" size="large" />
        </el-form-item>

        <el-form-item prop="confirmPassword">
          <div class="field-label">确认密码</div>
          <el-input
            v-model="form.confirmPassword"
            type="password"
            show-password
            placeholder="请再次输入密码"
            size="large"
          />
        </el-form-item>

        <el-form-item>
          <div class="login-actions">
            <el-button class="btn-register" @click="goLogin">返回登录</el-button>
            <el-button type="primary" class="btn-login" :loading="submitting" @click="submit">注册</el-button>
          </div>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import http from '../api/http'

const router = useRouter()
const formRef = ref()
const submitting = ref(false)

const form = reactive({
  username: '',
  phone: '',
  email: '',
  password: '',
  confirmPassword: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1\d{10}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== form.password) callback(new Error('两次输入的密码不一致'))
        else callback()
      },
      trigger: 'blur'
    }
  ]
}

const goLogin = async () => {
  await router.push('/login')
}

const submit = async () => {
  try {
    await formRef.value?.validate()
  } catch (e) {
    // 表单校验不通过时，Element Plus 会阻止提交
    return
  }

  submitting.value = true
  try {
    const payload = {
      username: form.username,
      password: form.password,
      phone: form.phone,
      email: form.email
    }
    const { data } = await http.post('/system/auth/register', payload)
    if (data && data.code === 200) {
      ElMessage.success(data.msg || '注册成功')
      await router.push('/login')
      return
    }
    ElMessage.error((data && data.msg) || '注册失败')
  } catch (e) {
    const msg =
      e?.response?.data?.msg ||
      e?.message ||
      '注册请求失败（请检查后端是否启动、前端是否配置了 /api 代理）'
    ElMessage.error(msg)
  } finally {
    submitting.value = false
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
</style>
