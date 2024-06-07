<template>

  <el-dialog
    :model-value="store.showLogin"
    :title="dialogConfig.title"
    width="500px"
    :before-close="handleClose"
  >
    <div class="dialog-body">
      <el-form
        :rules="rules"
        :model="formData"
        label-width="auto"
        ref="formDataRef"
      >

        <el-form-item label="账号" prop="account">
<!--          placeholder 属性用于设置输入框的占位符文本-->
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
<!--        根据opType确定当前是属于登录还是注册-->
        <el-form-item
          label="确认密码"
          prop="confirmPassword"
          v-if="opType == 1"
        >
          <el-input
            type="password"
            placeholder="再次输入密码"
            v-model="formData.confirmPassword"
          />
        </el-form-item>

        <!-- 注册账户 -->
        <el-form-item label="邮箱" prop="email" v-if="opType == 1">
          <el-input placeholder="请输入邮箱" v-model="formData.email" />
        </el-form-item>

        <el-form-item label="昵称" prop="nickName" v-if="opType == 1">
          <el-input placeholder="请输入昵称" v-model="formData.accountName" />
        </el-form-item>



        <el-form-item>

          <div class="no-account" v-if="opType == 0">

            <el-button type="primary" style="background-color: var(--mainColor)"
                       class="a-link" @click="showPanel(1)">注册账号</el-button>
          </div>
          <div class="no-account" v-if="opType == 1" >

            <el-button type="primary" style="background-color: var(--mainColor)"
                       class="a-link" @click="showPanel(0)">返回登录</el-button>
          </div>
          <div class="no-account" v-if="opType == 2">

            <el-button type="primary" style="background-color: var(--mainColor)"
                       class="a-link" @click="showPanel(0)">返回登录</el-button>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button
            v-if="opType == 0"
            type="primary"
            style="background-color: var(--mainColor)"
            @click="login()"
          >
            登录
          </el-button>

          <el-button
            v-if="opType == 1"
            type="primary"
            style="background-color: var(--mainColor)"
            @click="register()"
            >注册</el-button
          >
          <el-button
            v-if="opType == 2"
            type="primary"
            style="background-color: var(--mainColor)"
            @click="login()"
            >重置密码</el-button
          >
        </el-form-item>
      </el-form>
    </div>

    <template #footer> </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, getCurrentInstance } from "vue";
import { ElMessageBox } from "element-plus";
import { useMainStore } from "../stores/index";

const { proxy } = getCurrentInstance();
const store = useMainStore();
//响应式对象,改变后模板里也改变
const dialogConfig = reactive({
  title: "登录",
});

const showLoginDialog = () => {
  store.showLogin = true;
};
defineExpose({
  showLoginDialog,
});

// 表单数据
const formData = reactive({
  // account: "",
  // password: "",
  // confirmPassword: "",
  // email: "",
  // rememberMe: 0,
  // nickName: "",
});




const rules = reactive({
  account: [
    { required: true, message: "请输入账号", trigger: "blur" },
    { min: 3, max: 20, message: "账号只能是3-20位", trigger: "blur" },
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 3, max: 20, message: "密码只能是3-15位", trigger: "blur" },
  ],
  confirmPassword: [
    { required: true, message: "请确认密码", trigger: "blur" },
    { validator: proxy.Validate.validatePass, trigger: "blur" },
  ],
  email: [
    { required: true, message: "请输入邮箱", trigger: "blur" },
    { min: 3, max: 30, message: "邮箱只能是3-30位", trigger: "blur" },
    { validator: proxy.Validate.validateEmail, trigger: "blur" },
  ],
  nickName: [{ required: true, message: "请输入昵称", trigger: "blur" }],
});




// 登录
//登录返回data内容
//     {
//       "code": 200,
//         "msg": "success",
//         "data": {
//       "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6NywiYXZhdGFyIjowLCJ1c2VybmFtZSI6IjExMSIsImp0aSI6IjNhNmJiY2I5LWZlZTUtNDYzZi1iYzQyLTg0YTI5MzdiZjVjMCIsImV4cCI6MTcxNzA3OTQ0NiwiaWF0IjoxNzE3MDc1ODQ2LCJzdWIiOiJQZXJpcGhlcmFscyIsImlzcyI6IlRpYW0ifQ.8MqpMPKT0myH_KVTro-o7XLxZkA7PU17gNLRVJBrGqQ"
//     }
//     }
const login = async () => {
  // 首先验证表单数据是否有效
  const valid = await formDataRef.value.validate();
  if (!valid) {
    proxy.Message.warning("请输入内容！");
    return;
  }

  try {
    const loginResult = await proxy.Request({
      url: "/account/login",
      params: {
        username: formData.account,
        password: formData.password,
      },
    });

    if (loginResult && loginResult.data) {
      // 存储 token 到 localStorage
      localStorage.setItem("token", loginResult.data.token);

      // 显示登录成功的提示信息
      proxy.Message.success("登录成功");

      // 立即获取用户详细信息
      const userInfoResult = await fetchUserInfo();
      if (userInfoResult) {
        // 更新 Vuex store 的状态
        store.loginUserInfo = userInfoResult;
        // 隐藏登录窗口
        store.showLogin = false;
      } else {
        throw new Error("Failed to fetch user info");
      }
    } else {
      throw new Error("No data returned on login");
    }
  } catch (error) {
    // 处理登录或获取用户信息过程中的错误
    proxy.Message.error(error.message || "登录失败");
  } finally {
    // 无论成功或失败，清空表单
    restForm();
  }
};
//{
//   "id": 1,
//       "username": "111",
//       "password": null,
//       "accountName": "323232",
//       "avatar": 0
// }
// 功能：获取用户详细信息
const fetchUserInfo = async () => {
  try {
    const result = await proxy.Request({
      url: "/account/Info",
      params: {
      },
    });

    if (result && result.data) {
      return result.data;
    } else {
      console.error('No data received from the server');
      return null;
    }
  } catch (error) {
    console.error('Error fetching account info:', error);
    return null;
  }
};

// 注册
const register = async () => {
  let result = await proxy.Request({
    url: "/account/register",
    params: {
      username: formData.account,
      password: formData.password,
      accountName: formData.accountName,
    },
    dataType: "json",
    errorCallback: () => {
      proxy.Message.error("注册失败");
    },
  });
  if (!result) return;
  proxy.Message.success("注册成功");
  showPanel(0);
  // console.log(result)
};

const formDataRef = ref(null);
// 重置表单
const restForm = () => {
  formDataRef.value.resetFields();
};

const handleClose = () => {
  ElMessageBox.confirm("确定退出？你所输入的内容将被清空")
    .then(() => {
      store.showLogin = false;
      restForm();
    })
    .catch(() => {
      // catch error
    });
};

const opType = ref(0);
// 切换注册、登录、找回密码
const showPanel = (type) => {
  opType.value = type;
  restForm();
  switch (type) {
    case 0:
      dialogConfig.title = "登录";
      break;
    case 1:
      dialogConfig.title = "注册";
      break;
    case 2:
      dialogConfig.title = "重置密码";
      break;
  }
};
</script>
<style lang="scss" scoped>
.dialog-body {
  .remember-panel {
    width: 100%;
  }
  .no-account {
    width: 100%;
    display: flex;
    justify-content: space-between;
  }
  .op-button {
    width: 100%;
  }
}
</style>
