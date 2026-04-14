<template>
  <el-container class="layout-root">
    <el-aside width="220px" class="layout-aside">
      <div class="brand-box">
        学生管理系统
      </div>
      <el-menu
        :default-active="activeMenu"
        background-color="transparent"
        text-color="#c9d4e7"
        active-text-color="#409eff"
        @select="handleMenuSelect"
      >
        <el-sub-menu index="system">
          <template #title>系统管理</template>
          <el-menu-item index="users">用户管理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="student">
          <template #title>学生管理</template>
          <el-menu-item index="students">学生列表</el-menu-item>
          <el-menu-item index="scores">成绩列表</el-menu-item>
          <el-menu-item index="courses">课程列表</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="layout-header">
        <span class="page-title">后台管理</span>
        <div class="header-actions">
          <span>当前用户：{{ username }}</span>
          <el-button type="danger" plain size="small" @click="logout">退出登录</el-button>
        </div>
      </el-header>
      <el-main class="layout-main">
        <el-card v-if="activeMenu === 'users'" class="panel-card">
          <template #header>用户管理</template>
          <el-button type="success" @click="openUserDialog()">新增</el-button>
          <el-button type="primary" @click="loadUsers">刷新</el-button>
          <el-table :data="users" style="margin-top: 12px;">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="username" label="用户名" />
            <el-table-column prop="role" label="角色" />
            <el-table-column label="操作" width="180">
              <template #default="{ row }">
                <el-button link type="primary" @click="openUserDialog(row)">编辑</el-button>
                <el-button link type="danger" @click="removeUser(row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            style="margin-top: 12px;"
            background
            layout="total, prev, pager, next"
            :total="userPage.total"
            :page-size="userPage.size"
            :current-page="userPage.current"
            @current-change="onUserPageChange"
          />
        </el-card>

        <el-card v-if="activeMenu === 'students'" class="panel-card">
          <template #header>学生列表</template>
          <el-button type="success" @click="openStudentDialog()">新增</el-button>
          <el-button type="primary" @click="loadStudents">刷新</el-button>
          <el-table :data="students" style="margin-top: 12px;">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="studentNo" label="学号" />
            <el-table-column prop="name" label="姓名" />
            <el-table-column prop="age" label="年龄" />
            <el-table-column prop="gender" label="性别" />
            <el-table-column label="操作" width="180">
              <template #default="{ row }">
                <el-button link type="primary" @click="openStudentDialog(row)">编辑</el-button>
                <el-button link type="danger" @click="removeStudent(row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            style="margin-top: 12px;"
            background
            layout="total, prev, pager, next"
            :total="studentPage.total"
            :page-size="studentPage.size"
            :current-page="studentPage.current"
            @current-change="onStudentPageChange"
          />
        </el-card>

        <el-card v-if="activeMenu === 'scores'" class="panel-card">
          <template #header>成绩列表</template>
          <el-button type="success" @click="openScoreDialog()">新增</el-button>
          <el-button type="primary" @click="loadScores">刷新</el-button>
          <el-table :data="scores" style="margin-top: 12px;">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="studentId" label="学生ID" />
            <el-table-column prop="courseId" label="课程ID" />
            <el-table-column prop="score" label="成绩" />
            <el-table-column label="操作" width="180">
              <template #default="{ row }">
                <el-button link type="primary" @click="openScoreDialog(row)">编辑</el-button>
                <el-button link type="danger" @click="removeScore(row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            style="margin-top: 12px;"
            background
            layout="total, prev, pager, next"
            :total="scorePage.total"
            :page-size="scorePage.size"
            :current-page="scorePage.current"
            @current-change="onScorePageChange"
          />
        </el-card>

        <el-card v-if="activeMenu === 'courses'" class="panel-card">
          <template #header>课程列表</template>
          <el-button type="success" @click="openCourseDialog()">新增</el-button>
          <el-button type="primary" @click="loadCourses">刷新</el-button>
          <el-table :data="courses" style="margin-top: 12px;">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="courseCode" label="课程编号" />
            <el-table-column prop="courseName" label="课程名" />
            <el-table-column prop="credit" label="学分" />
            <el-table-column label="操作" width="180">
              <template #default="{ row }">
                <el-button link type="primary" @click="openCourseDialog(row)">编辑</el-button>
                <el-button link type="danger" @click="removeCourse(row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            style="margin-top: 12px;"
            background
            layout="total, prev, pager, next"
            :total="coursePage.total"
            :page-size="coursePage.size"
            :current-page="coursePage.current"
            @current-change="onCoursePageChange"
          />
        </el-card>
      </el-main>
    </el-container>
  </el-container>

  <el-dialog v-model="userDialogVisible" :title="userEditId ? '编辑用户' : '新增用户'" width="420px">
    <el-form :model="userForm" label-width="80px">
      <el-form-item label="用户名"><el-input v-model="userForm.username" /></el-form-item>
      <el-form-item label="密码"><el-input v-model="userForm.password" type="password" /></el-form-item>
      <el-form-item label="角色"><el-input v-model="userForm.role" /></el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="userDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitUser">确定</el-button>
    </template>
  </el-dialog>

  <el-dialog v-model="studentDialogVisible" :title="studentEditId ? '编辑学生' : '新增学生'" width="480px">
    <el-form :model="studentForm" label-width="90px">
      <el-form-item label="学号"><el-input v-model="studentForm.studentNo" /></el-form-item>
      <el-form-item label="姓名"><el-input v-model="studentForm.name" /></el-form-item>
      <el-form-item label="年龄"><el-input v-model.number="studentForm.age" /></el-form-item>
      <el-form-item label="性别"><el-input v-model="studentForm.gender" /></el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="studentDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitStudent">确定</el-button>
    </template>
  </el-dialog>

  <el-dialog v-model="courseDialogVisible" :title="courseEditId ? '编辑课程' : '新增课程'" width="480px">
    <el-form :model="courseForm" label-width="90px">
      <el-form-item label="课程编号"><el-input v-model="courseForm.courseCode" /></el-form-item>
      <el-form-item label="课程名"><el-input v-model="courseForm.courseName" /></el-form-item>
      <el-form-item label="学分"><el-input v-model.number="courseForm.credit" /></el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="courseDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitCourse">确定</el-button>
    </template>
  </el-dialog>

  <el-dialog v-model="scoreDialogVisible" :title="scoreEditId ? '编辑成绩' : '新增成绩'" width="480px">
    <el-form :model="scoreForm" label-width="90px">
      <el-form-item label="学生ID"><el-input v-model.number="scoreForm.studentId" /></el-form-item>
      <el-form-item label="课程ID"><el-input v-model.number="scoreForm.courseId" /></el-form-item>
      <el-form-item label="成绩"><el-input v-model.number="scoreForm.score" /></el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="scoreDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitScore">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import http from '../api/http'

const router = useRouter()
const username = localStorage.getItem('username') || '未登录'
const activeMenu = ref('users')
const users = ref([])
const students = ref([])
const courses = ref([])
const scores = ref([])

const userPage = reactive({ current: 1, size: 10, total: 0 })
const studentPage = reactive({ current: 1, size: 10, total: 0 })
const coursePage = reactive({ current: 1, size: 10, total: 0 })
const scorePage = reactive({ current: 1, size: 10, total: 0 })

const userDialogVisible = ref(false)
const studentDialogVisible = ref(false)
const courseDialogVisible = ref(false)
const scoreDialogVisible = ref(false)
const userEditId = ref(null)
const studentEditId = ref(null)
const courseEditId = ref(null)
const scoreEditId = ref(null)

const userForm = reactive({ username: '', password: '', role: 'STUDENT' })
const studentForm = reactive({ studentNo: '', name: '', age: 18, gender: '男' })
const courseForm = reactive({ courseCode: '', courseName: '', credit: 2 })
const scoreForm = reactive({ studentId: 1, courseId: 1, score: 60 })

const handleMenuSelect = async (index) => {
  activeMenu.value = index
  if (index === 'users') {
    userPage.current = 1
  }
  if (index === 'students') {
    studentPage.current = 1
  }
  if (index === 'scores') {
    scorePage.current = 1
  }
  if (index === 'courses') {
    coursePage.current = 1
  }
  if (index === 'users') await loadUsers()
  if (index === 'students') await loadStudents()
  if (index === 'scores') await loadScores()
  if (index === 'courses') await loadCourses()
}

const loadUsers = async () => {
  try {
    const { data } = await http.get('/system/users/page', {
      params: { current: userPage.current, size: userPage.size }
    })
    const pageData = data.data || { records: [], total: 0, current: 1, size: 10 }
    users.value = pageData.records || []
    userPage.total = Number(pageData.total || 0)
    userPage.current = Number(pageData.current || 1)
  } catch (e) {
    const { data } = await http.get('/system/users')
    const all = data.data || []
    const start = (userPage.current - 1) * userPage.size
    const end = start + userPage.size
    users.value = all.slice(start, end)
    userPage.total = all.length
    ElMessage.warning('用户分页接口异常，已切换列表兜底')
  }
}

const loadStudents = async () => {
  try {
    const { data } = await http.get('/biz/students/page', {
      params: { current: studentPage.current, size: studentPage.size }
    })
    const pageData = data.data || { records: [], total: 0, current: 1, size: 10 }
    students.value = pageData.records || []
    studentPage.total = Number(pageData.total || 0)
    studentPage.current = Number(pageData.current || 1)
  } catch (e) {
    const { data } = await http.get('/biz/students')
    const all = data.data || []
    const start = (studentPage.current - 1) * studentPage.size
    const end = start + studentPage.size
    students.value = all.slice(start, end)
    studentPage.total = all.length
    ElMessage.warning('学生分页接口异常，已切换列表兜底')
  }
}

const loadCourses = async () => {
  try {
    const { data } = await http.get('/biz/courses/page', {
      params: { current: coursePage.current, size: coursePage.size }
    })
    const pageData = data.data || { records: [], total: 0, current: 1, size: 10 }
    courses.value = pageData.records || []
    coursePage.total = Number(pageData.total || 0)
    coursePage.current = Number(pageData.current || 1)
  } catch (e) {
    const { data } = await http.get('/biz/courses')
    const all = data.data || []
    const start = (coursePage.current - 1) * coursePage.size
    const end = start + coursePage.size
    courses.value = all.slice(start, end)
    coursePage.total = all.length
    ElMessage.warning('课程分页接口异常，已切换列表兜底')
  }
}

const loadScores = async () => {
  try {
    const { data } = await http.get('/biz/scores/page', {
      params: { current: scorePage.current, size: scorePage.size }
    })
    const pageData = data.data || { records: [], total: 0, current: 1, size: 10 }
    scores.value = pageData.records || []
    scorePage.total = Number(pageData.total || 0)
    scorePage.current = Number(pageData.current || 1)
  } catch (e) {
    const { data } = await http.get('/biz/scores')
    const all = data.data || []
    const start = (scorePage.current - 1) * scorePage.size
    const end = start + scorePage.size
    scores.value = all.slice(start, end)
    scorePage.total = all.length
    ElMessage.warning('成绩分页接口异常，已切换列表兜底')
  }
}

const onUserPageChange = async (page) => {
  userPage.current = page
  await loadUsers()
}

const onStudentPageChange = async (page) => {
  studentPage.current = page
  await loadStudents()
}

const onCoursePageChange = async (page) => {
  coursePage.current = page
  await loadCourses()
}

const onScorePageChange = async (page) => {
  scorePage.current = page
  await loadScores()
}

const openUserDialog = (row) => {
  userEditId.value = row?.id || null
  userForm.username = row?.username || ''
  userForm.password = ''
  userForm.role = row?.role || 'STUDENT'
  userDialogVisible.value = true
}

const openStudentDialog = (row) => {
  studentEditId.value = row?.id || null
  studentForm.studentNo = row?.studentNo || ''
  studentForm.name = row?.name || ''
  studentForm.age = row?.age ?? 18
  studentForm.gender = row?.gender || '男'
  studentDialogVisible.value = true
}

const openCourseDialog = (row) => {
  courseEditId.value = row?.id || null
  courseForm.courseCode = row?.courseCode || ''
  courseForm.courseName = row?.courseName || ''
  courseForm.credit = row?.credit ?? 2
  courseDialogVisible.value = true
}

const openScoreDialog = (row) => {
  scoreEditId.value = row?.id || null
  scoreForm.studentId = row?.studentId ?? 1
  scoreForm.courseId = row?.courseId ?? 1
  scoreForm.score = row?.score ?? 60
  scoreDialogVisible.value = true
}

const submitUser = async () => {
  const payload = { username: userForm.username, role: userForm.role }
  if (userForm.password) payload.password = userForm.password
  const { data } = userEditId.value
    ? await http.put(`/system/users/${userEditId.value}`, payload)
    : await http.post('/system/users', { ...payload, password: userForm.password || '123456' })
  if (data.code === 200) {
    ElMessage.success(data.msg || '操作成功')
    userDialogVisible.value = false
    await loadUsers()
  } else {
    ElMessage.error(data.msg || '操作失败')
  }
}

const submitStudent = async () => {
  const payload = { ...studentForm }
  const { data } = studentEditId.value
    ? await http.put(`/biz/students/${studentEditId.value}`, payload)
    : await http.post('/biz/students', payload)
  if (data.code === 200) {
    ElMessage.success(data.msg || '操作成功')
    studentDialogVisible.value = false
    await loadStudents()
  } else {
    ElMessage.error(data.msg || '操作失败')
  }
}

const submitCourse = async () => {
  const payload = { ...courseForm }
  const { data } = courseEditId.value
    ? await http.put(`/biz/courses/${courseEditId.value}`, payload)
    : await http.post('/biz/courses', payload)
  if (data.code === 200) {
    ElMessage.success(data.msg || '操作成功')
    courseDialogVisible.value = false
    await loadCourses()
  } else {
    ElMessage.error(data.msg || '操作失败')
  }
}

const submitScore = async () => {
  const payload = { ...scoreForm }
  const { data } = scoreEditId.value
    ? await http.put(`/biz/scores/${scoreEditId.value}`, payload)
    : await http.post('/biz/scores', payload)
  if (data.code === 200) {
    ElMessage.success(data.msg || '操作成功')
    scoreDialogVisible.value = false
    await loadScores()
  } else {
    ElMessage.error(data.msg || '操作失败')
  }
}

const removeUser = async (id) => {
  const { data } = await http.delete(`/system/users/${id}`)
  if (data.code === 200) {
    ElMessage.success('删除成功')
    await loadUsers()
  } else {
    ElMessage.error(data.msg || '删除失败')
  }
}

const removeStudent = async (id) => {
  const { data } = await http.delete(`/biz/students/${id}`)
  if (data.code === 200) {
    ElMessage.success('删除成功')
    await loadStudents()
  } else {
    ElMessage.error(data.msg || '删除失败')
  }
}

const removeCourse = async (id) => {
  const { data } = await http.delete(`/biz/courses/${id}`)
  if (data.code === 200) {
    ElMessage.success('删除成功')
    await loadCourses()
  } else {
    ElMessage.error(data.msg || '删除失败')
  }
}

const removeScore = async (id) => {
  const { data } = await http.delete(`/biz/scores/${id}`)
  if (data.code === 200) {
    ElMessage.success('删除成功')
    await loadScores()
  } else {
    ElMessage.error(data.msg || '删除失败')
  }
}

const logout = async () => {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  ElMessage.success('已退出登录')
  await router.push('/login')
}

onMounted(async () => {
  await loadUsers()
})
</script>

<style scoped>
.layout-root {
  height: 100vh;
  background: #f3f6fb;
}

.layout-aside {
  background: linear-gradient(180deg, #1f3a5f 0%, #1b2d48 100%);
  color: #fff;
  box-shadow: 3px 0 16px rgba(15, 23, 42, 0.15);
}

.brand-box {
  padding: 18px 16px;
  font-weight: 700;
  font-size: 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.12);
}

:deep(.el-menu) {
  border-right: none;
}

:deep(.el-sub-menu__title),
:deep(.el-menu-item) {
  border-radius: 8px;
  margin: 4px 10px;
}

.layout-header {
  margin: 14px 16px 0;
  background: #fff;
  border-radius: 12px;
  padding: 0 18px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 4px 16px rgba(15, 23, 42, 0.07);
}

.page-title {
  font-size: 18px;
  font-weight: 700;
  color: #1f2937;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
  color: #475569;
}

.layout-main {
  padding: 16px;
}

.panel-card {
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(15, 23, 42, 0.07);
}

:deep(.el-card__header) {
  font-weight: 700;
}
</style>
