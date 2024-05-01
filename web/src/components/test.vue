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
import { ElForm, ElFormItem, ElInput, ElButton } from 'element-plus';
import { ElMessage } from 'element-plus';
import { ref, reactive, getCurrentInstance } from "vue";
const { proxy } = getCurrentInstance();


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
    const response = await proxy.Request({
      url: '/api/login',
      params: {
        username: ruleForm.username,
        password: ruleForm.password,
      },
    });
    if (response  && response.status === 200) {
      proxy.Message.success("登录成功");
      // 登录成功后的操作，例如跳转到首页
      // router.push('/home');
    } else {
      proxy.Message.error(response.data.message || "登录失败");
    }
  } catch (error) {
    if (error && error.response) {
      proxy.Message.error('登录失败：' + error.response.data.message);
    } else {
      proxy.Message.error('网络错误：' + error.message);
    }
  }
};




</script>
