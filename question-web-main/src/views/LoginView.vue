<template>
  <div class="login-view">
    <el-form
        :rules="rules"
        :model="formData"
        label-width="auto"
        ref="formDataRef"
        class="login-form"
    >
      <el-form-item label="账号" prop="account">
        <el-input placeholder="请输入账号" v-model="formData.account" />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input
            type="password"
            placeholder="请输入密码"
            v-model="formData.password"
        />
      </el-form-item>

      <!-- 确认密码 -->
      <el-form-item
          label="确认密码"
          prop="confirmPassword"
          v-if="opType === 1"
      >
        <el-input
            type="password"
            placeholder="再次输入密码"
            v-model="formData.confirmPassword"
        />
      </el-form-item>

      <!-- 注册账户 -->
      <el-form-item label="邮箱" prop="email" v-if="opType === 1">
        <el-input placeholder="请输入邮箱" v-model="formData.email" />
      </el-form-item>

      <el-form-item label="昵称" prop="nickName" v-if="opType === 1">
        <el-input placeholder="请输入昵称" v-model="formData.nickName" />
      </el-form-item>

      <el-form-item>
        <el-button
            type="primary"
            style="background-color: var(--mainColor)"
            @click="handleOperation(opType)"
        >
          {{ opType === 0 ? "登录" : opType === 1 ? "注册" : "重置密码" }}
        </el-button>
        <el-button
            v-if="opType !== 0"
            type="default"
            @click="showPanel(0)"
        >
          返回登录
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import { useMainStore } from '../stores/index';

const store = useMainStore();
const opType = ref(0); // 0: Login, 1: Register, 2: Reset Password
const formData = reactive({
  account: '',
  password: '',
  confirmPassword: '',
  email: '',
  nickName: ''
});
const formDataRef = ref(null);

const rules = reactive({
  account: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 3, max: 20, message: '账号只能是3-20位', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 3, max: 20, message: '密码只能是3-15位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: (rule, value, callback) => {
        if (value !== formData.password) {
          callback(new Error('两次输入的密码不匹配'));
        } else {
          callback();
        }
      }, trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { min: 3, max: 30, message: '邮箱只能是3-30位', trigger: 'blur' }
  ],
  nickName: [{ required: true, message: '请输入昵称', trigger: 'blur' }]
});

const handleOperation = async (type) => {
  const valid = await formDataRef.value.validate();
  if (!valid) return;
  // 进行登录、注册或密码重置的逻辑处理
  ElMessage.success(type === 0 ? '登录成功' : type === 1 ? '注册成功' : '密码已重置');
};

const showPanel = (type) => {
  opType.value = type;
  formDataRef.value.resetFields();
};
</script>

<style scoped>
.login-view {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
}

.login-form {
  display: flex;
  flex-direction: column;
}
</style>
