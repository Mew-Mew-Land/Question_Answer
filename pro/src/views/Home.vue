<template>
  <el-container>
    <el-aside width="200px">
      <SideMenu></SideMenu>
    </el-aside>
    <el-container>
      <el-header>
        <strong>后台管理系统</strong>
        <div class="header-avatar">
          <el-avatar size="medium" :src="userInfo.avatar"></el-avatar>
          <el-dropdown>
            <span class="el-dropdown-link">
              {{userInfo.username}}<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <router-link :to="{name: 'UserCenter'}">个人中心</router-link>
              </el-dropdown-item>
              <el-dropdown-item @click="logout">退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-link href="https://markerhub.com" target="_blank">网站</el-link>
          <el-link href="https://space.bilibili.com/13491144" target="_blank">B站</el-link>
        </div>
      </el-header>
      <el-main>
        <Tabs></Tabs>
        <div style="margin: 0 15px;">
          <router-view/>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { ref } from 'vue'; // Vue 3响应式API
import SideMenu from "./inc/SideMenu";
import Tabs from "./inc/Tabs";

export default {
  name: "Home",
  components: {
    SideMenu, Tabs
  },
  setup() {
    // 使用Vue 3的Composition API
    const userInfo = ref({
      id: "",
      username: "",
      avatar: ""
    });

    // 获取用户信息
    const getUserInfo = () => {
      axios.get("/sys/userInfo").then(res => {
        userInfo.value = res.data.data;
      });
    };

    // 用户登出
    const logout = () => {
      axios.post("/logout").then(() => {
        localStorage.clear();
        sessionStorage.clear();

        // 重置状态
        this.$store.commit("resetState");

        // 跳转到登录页面
        this.$router.push("/login");
      });
    };

    // 在组件创建时获取用户信息
    getUserInfo();

    // 返回响应式数据和方法
    return {
      userInfo,
      logout
    };
  }
};
</script>
