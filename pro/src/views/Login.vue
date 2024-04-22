<template >
<!--  //行布局,弹性布局,子元素居中-->
  <el-row type="flex" class="row-bg" justify="center">
<!--    //展示图片的列组件,后续是长度-->
    <el-col :xl="6" :lg="7">
      <div class="login-form">
        <h2>欢迎</h2>

        <el-image style="width: 180px; height: 180px" :src="require('@/assets/logo.png')"></el-image>
        <p>这是图片</p>
        <p>这是介绍</p>
      </div>
    </el-col>

    <el-col :xl="6" :lg="7">
<!--      //表单,label-position标签位置居右,rules设定响应式对象 -->
      <el-form label-position="right" :rules="rules" label-width="80px" :model="loginForm" ref="loginFormRef">
        <el-form-item label="用户名" prop="username" style="width: 380px;">
          <el-input v-model="loginForm.username"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" style="width: 380px;">
          <el-input type="password" v-model="loginForm.password"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="code" style="width: 380px;">
          <el-input v-model="loginForm.code" style="width: 172px; float: left;" maxlength="5"></el-input>
<!--          获取验证码函数-->
          <el-image class="captchaImg" :src="captchaImg" @click="getCaptcha"></el-image>
        </el-form-item>
        <el-form-item>
<!--          提交登录函数-->
          <el-button type="primary" @click="submitForm">登录</el-button>
          <el-button @click="getPass">忘记密码</el-button>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>

</template>


<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import qs from 'qs';
import axios from 'axios';
import store from "@/store";
import router from "@/router";
// reactive创建响应式对象,内容改变时组件自动更新
const loginForm = reactive({
  username: '',
  password: '',
  code: '',
  token: '',
});
// reactive创建响应式对象,内容改变时组件自动更新,设定输入的规则
const rules = reactive({
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { min: 5, max: 5, message: '验证码为5个字符', trigger: 'blur' }
  ],
});
//ref 函数创建了一个响应式引用 (单个响应式数据)
const captchaImg = ref('');

const loginFormRef = ref(null);
// 异步函数
const getCaptcha = async () => {
  //向后端/captcha发送GET请求,await 来等待请求完成
  //mock.js拦截模拟后端
  const response = await axios.get('/captcha');
  loginForm.token = response.data.data.token;
  captchaImg.value = response.data.data.captchaImg;
};

  const submitForm = () => {
    //对loginFormRef.value实例对象的验证(就是上面设定的rulers规则),结果为validation
    loginFormRef.value.validate((validation) => {
      if (validation) {
        //axios是一个基于Promise的HTTP客户端
        //axios.post是调用axios来执行一个POST请求的方法。'/login?'是请求的目标URL，问号?表示查询参数的开始。
        //qs.stringify(loginForm)将一个对象（在这里loginForm对象）转换成一个查询字符串。
        //then方法是Promise的一部分。axios.post返回一个Promise，这表示某个时候将来它会完成请求,并返回正确的响应或者失败
        axios.post('/login', loginForm).then((response) => {
          // 假设JWT在响应的data中返回
          const jwt = response.data.data.jwt;
          // 模拟将JWT保存到Vuex状态树中
          store.commit("SET_TOKEN", jwt);
          // 为了演示，这里可以暂时使用localStorage模拟
          localStorage.setItem('jwt', jwt);

          router.push("/testhome");
        }).catch(() => {alert("开始验证失败");
          getCaptcha();
          ElMessage.error('登录错误!!');
        });

      } else {
        alert("没进入开始验证");
        getCaptcha();
        ElMessage.error('error submit!!');
      }
    });
  };

onMounted(() => {
  getCaptcha();
});
</script>


<style scoped>
.el-col {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  text-align: center;
}
.el-row {
  height: 100%;
  background-color: #fafafa;
}
.el-divider {
  height: 200px;
}
.captchaImg {
  float: left;
  margin-left: 8px;
  border-radius: 4px;
}
</style>