<template>
  <div class="login-container">
    <!-- 登录表单 -->
    <el-form ref="ruleFormRef" :model="ruleForm" :rules="rules" label-width="80px">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="ruleForm.username"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input type="password" v-model="ruleForm.password"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="login">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { ElForm, ElFormItem, ElInput, ElButton } from 'element-plus';
import { ElMessage } from 'element-plus';
import axios from 'axios';



const ruleForm = ref({
  username: '',
  password: ''
});

const rules = ref({
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
});

const ruleFormRef = ref(null);

const login = () => {
  ruleFormRef.value.validate(valid => {
    if (valid) {
      mockLogin();
    }
  });
};

const mockLogin = async () => {
  try {
    const response = await axios.post('/api/login', {
      username: ruleForm.value.username,
      password: ruleForm.value.password
    });
    // 检查状态码来决定是否登录成功
    if (response.data.status === 200) {
      ElMessage.success('登录成功');
      // 例如：router.push('/home');
    } else {
      ElMessage.error(response.data.message);
    }
  } catch (error) {
    // 通常在 catch 中处理网络错误，而不是登录逻辑错误
    ElMessage.error('网络错误：' + error.message);
  }
};


</script>
