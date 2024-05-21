<template>
<!--  :model-value用于控制对话框的显示和隐藏状态。-->
<!--  :title 属性用于设置对话框的标题内容。-->
<!--  width 属性用于设置对话框的宽度。-->
<!--  :before-close 是一个事件属性，用于在对话框关闭之前执行特定的逻辑。-->
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
<!--        prop 属性用于设置表单项的字段名，用于在表单校验时确定校验规则和获取表单数据-->
<!--        注册登录两个页面公共部分:账号/密码-->
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
          <el-input placeholder="请输入昵称" v-model="formData.nickName" />
        </el-form-item>

        <!-- <el-form-item label="验证码" prop="checkCode">
          <el-input placeholder="请输入验证码" v-model="form.checkCode" />
        </el-form-item> -->

        <el-form-item>
          <!-- <div class="remember-panel" v-if="opType == 0">
            <el-checkbox v-model="formData.rememberMe">记住我</el-checkbox>
          </div> -->
          <div class="no-account" v-if="opType == 0">
<!--            <span class="a-link" @click="showPanel(1)">注册</span>-->
            <el-button type="primary" style="background-color: var(--mainColor)"
                       class="a-link" @click="showPanel(1)">注册账号</el-button>
          </div>
          <div class="no-account" v-if="opType == 1" >
<!--            <span class="a-link" @click="showPanel(0)">返回登录</span>-->
            <el-button type="primary" style="background-color: var(--mainColor)"
                       class="a-link" @click="showPanel(0)">返回登录</el-button>
          </div>
          <div class="no-account" v-if="opType == 2">
<!--            <span class="a-link" @click="showPanel(0)">返回登录</span>-->
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
import { auth } from "../utils/api.utils";
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
const login = () => {
  formDataRef.value.validate(async (vaild) => {
    if (!vaild) {
      proxy.Message.warning("请输入内容！");
      return;
    }

    let result = await proxy.Request({
      url: "/auth/login",
      params: {
        username: formData.account,   
        password: formData.password,
      },
      dataType: "json",
      errorCallback: () => {
        proxy.Message.error("登录失败");
      },
    });

    if (!result) return;

    // 对返回的用户信息进行处理，转换 ISO 格式的时间为可读格式
    result.data.userInfo.createTime = proxy.TransformIsoDate(result.data.userInfo.createTime);
    result.data.userInfo.updateTime = proxy.TransformIsoDate(result.data.userInfo.updateTime);

    // 将用户信息和 token 存储到 localStorage 中
    localStorage.setItem("userInfo", JSON.stringify(result.data.userInfo));
    localStorage.setItem("token", result.data.token);

    // 显示登录成功的提示信息
    proxy.Message.success("登录成功");

    // 隐藏登录窗口，更新登录状态和用户信息
    store.showLogin = false;
    store.loginUserInfo = result.data.userInfo;

    // 清空表单数据
    restForm();
  });


};

// 注册
const register = async () => {
  let result = await proxy.Request({
    url: "/auth/register",
    params: {
      username: formData.account,
      password: formData.password,
      passwordRepeat: formData.confirmPassword,
      email: formData.email,
      nickName: formData.nickName,
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
