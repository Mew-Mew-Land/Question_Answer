<template>
  <el-container>
    <el-aside width="200px">

      <SideMenu></SideMenu>

    </el-aside>
    <el-container>
      <!-- 头部容器组件 -->
      <el-header>
        <!-- 使用 <strong> 标签加粗 -->
        <strong>后台管理</strong>
        <!-- 头像和下拉菜单的容器 -->
        <div class="header-avatar">
          <!-- el-avatar 是 头像组件 -->
          <!-- :src="userInfo.avatar" 通过绑定 src 属性来动态显示用户头像 -->
          <el-avatar size="medium" :src="userInfo.avatar"></el-avatar>
<!--          {{userInfo.username}}-->
          <!-- el-dropdown 是 下拉菜单组件 -->
          <el-dropdown trigger="click" >
    <span class="el-dropdown-link">
      点击这里<i class="el-icon-arrow-down el-icon--right"></i>
    </span>
            <template #dropdown>
              <el-dropdown-menu >
                <el-dropdown-item>
                  <router-link :to="{name: 'UserCenter'}">个人中心</router-link>
                </el-dropdown-item>
                <el-dropdown-item @click.native="logout">退出</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>

          <!-- el-link 是 Element Plus UI 库中的链接组件，这里用作普通按钮使用 -->
          <el-link>按钮</el-link>
          <el-link>按钮</el-link>
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


<script >
import { onMounted, reactive } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useStore } from 'vuex';
import axios from 'axios';
import SideMenu from "./inc/SideMenu";
import Tabs from "./inc/Tabs";

export default {
  name: "Home",
  components: {
    SideMenu, Tabs
  },
  data() {
    return {
      userInfo: {
        id: "",
        username: "",
        avatar: ""
      }
    }
  },
  created() {
    this.getUserInfo()
  },
  methods: {
    getUserInfo() {
      this.$axios.get("/sys/userInfo").then(res => {
        this.userInfo = res.data.data
      })
    },
    logout() {
      this.$axios.post("/logout").then(res => {
        localStorage.clear()
        sessionStorage.clear()

        this.$store.commit("resetState")

        this.$router.push("/login")
      })
    }
  }
}
</script>


<style scoped>
.el-container {
  padding: 0;
  margin: 0;
  height: 100%;
}

.header-avatar {
  float: right;
  width: 210px;
  display: flex;
  justify-content: space-around;
  align-items: center;
}

.el-dropdown-link {
  cursor: pointer;
}

.el-header {
  background-color: #17B3A3;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #D3DCE6;
  color: #333;
  line-height: 200px;
}

.el-main {
  color: #333;
  padding: 0;
}

a {
  text-decoration: none;
}
</style>
