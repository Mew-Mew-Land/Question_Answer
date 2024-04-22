<template>
  <!-- 头部导航 -->
  <div class="header">
    <div class="header-container">
      <div class="header-body container-body">
        <div class="logo">
          <img src="../icon/logo.png" alt="" srcset="" />
        </div>
        <div class="navbar">
          <!-- 板块 -->
          <div class="board">
            <RouterLink to="/" class="nav-link">问答</RouterLink>
            <!-- <RouterLink to="/article" class="nav-link">文章</RouterLink> -->
          </div>
          <div class="nav-right">
            <!-- 搜索框 -->
            <div class="search">
              <el-input
                size=""
                clearable
                placeholder="请输入关键词查找"
                v-model="input"
                @keyup.enter="search"
              >
                <template #suffix>
                  <span
                    class="iconfont icon-sousuo"
                    @click="search"
                    @blur=""
                  ></span>
                </template>
              </el-input>
            </div>
            <!-- 用户区域 -->
            <div class="user-operation">
              <el-button
                v-if="!userInfo"
                type="primary"
                class="theme-color"
                style="background-color: var(--mainColor)"
                @click="loginAndRegister()"
                >注册登录</el-button
              >
              <div class="user-info" v-else>
                <el-dropdown>
                  <el-button
                    type="primary"
                    style="background-color: var(--mainColor)"
                  >
                    发布
                    <span class="iconfont icon-xialacaidan"></span>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item @click="newPost()"
                        >提问题</el-dropdown-item
                      >
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
                <!-- 消息图标 -->
                <RouterLink
                  to="/userMessage"
                  style="text-decoration: none; color: var(--mainColor)"
                >
                  <span class="iconfont icon-xiaoxi"></span>
                </RouterLink>

                <!-- 头像 -->
                <el-dropdown>
                  <span class="el-dropdown-link">
                    <Avatar></Avatar>
                  </span>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item @click="toCenter()"
                        >我的主页</el-dropdown-item
                      >
                      <el-dropdown-item>个人设置</el-dropdown-item>
                      <el-dropdown-item @click="logout()"
                        >退出登录</el-dropdown-item
                      >
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>
            </div>
          </div>
        </div>
        <div class="navbar-m">
          <!-- 头像 -->
          <el-dropdown>
            <span class="el-dropdown-link">
              <Avatar></Avatar>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="toCenter()"
                  >我的主页</el-dropdown-item
                >
                <el-dropdown-item @click="logout()">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <span class="iconfont menu"></span>
        </div>
      </div>
    </div>

    <div class="category-tags-container container-body" v-if="isHome">
      <span class="categories-list">
        <span

          :class="[
            'categories-list-item',
            activeBoard == null ? 'categories-active' : '',
          ]"
          @click="allQuestion()"
          >全部</span
        >
        <span
          :class="[
            'categories-list-item',
            item.boardId == activeBoard ? 'categories-active' : '',
          ]"
          v-for="item in boardList"
          @click="boradClickHandler(item.boardId)"
        >
          {{ item.boardName }}
        </span>
      </span>
    </div>
  </div>

  <!-- 显示区域 -->
  <div class="body-content">
    <RouterView></RouterView>
  </div>
  <!-- 登录注册 -->
  <LoginReg ref="loginRegRef"></LoginReg>
  <!-- 回到顶部 -->
  <el-backtop :right="100" :bottom="100"></el-backtop>
</template>

<script setup>
import LoginReg from "../components/LoginReg.vue";
import { ref, watch, getCurrentInstance, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useMainStore } from "../stores/index";
import { board } from "../utils/api.utils";
const { proxy } = getCurrentInstance();
const route = useRoute();
const router = useRouter();
const store = useMainStore();

const logout = () => {
  proxy.Confirm("确认退出？", () => {
    userInfo.value = {};
    localStorage.clear("userInfo");
    localStorage.clear("token");
    store.loginUserInfo = "";
    // 重新加载页面
    // location.reload();
  });
};

// 获取板块信息
const boardList = ref(null);
const getBoardList = async () => {
  let result = await proxy.Request({
    url: board.allBoard,
    errorCallback: () => {
      proxy.Message.error("查询板块失败");
    },
  });
  if (!result) return;
  boardList.value = result.data;
  store.boardList = result.data;
};

const loginRegRef = ref();
// 登录注册
const loginAndRegister = () => {
  loginRegRef.value.showDialog();
};

const toCenter = () => {
  router.push(`/user/${store.loginUserInfo.userId}`);
};

const newPost = () => {
  router.push("/newPost");
};
const isHome = ref(true);

// 当前选中的板块
const activeBoard = ref(null);

// 切换板块
const boradClickHandler = (boardId) => {
  router.push(`/faq/${boardId}`);
};

// 切换回首页
const allQuestion = () => {
  router.push("/");
};

// 搜索
const input = ref("");
const search = () => {
  if (!input.value) {
    proxy.Message.warning("请输入关键词");
    return;
  }
  router.push(`/search/${input.value}`);
  input.value = "";
};

onMounted(() => {
  getBoardList();
});
watch(
  () => route.params.boardId,
  (newVal, oldVal) => {
    store.boardId = newVal;
  },
  { immediate: true, deep: true }
);

watch(
  () => store.boardId,
  (newVal, oldVal) => {
    activeBoard.value = newVal;
  },
  { immediate: true, deep: true }
);

// 监听用户信息变化
const userInfo = ref({});
watch(
  () => store.loginUserInfo,
  (newVal, oldVal) => {
    if (newVal != undefined && newVal != null) {
      userInfo.value = newVal;
    } else {
      userInfo.value = {};
    }
  },
  { immediate: true, deep: true }
);
</script>
<style lang="scss" scoped>
.header {
  width: 100%;
  background-color: #fff;
  position: sticky;
  top: 0;
  left: 0;
  z-index: 999;
  .header-container {
    width: 100%;
    border-bottom: #efefef 1px solid;
    .header-body {
      display: flex;
      height: 65px;
      align-items: center;
      .logo {
        width: 50px;
        height: 50px;
        margin-left: 8px;
        img {
          width: 100%;
        }
      }
      .navbar {
        display: flex;
        justify-content: space-between;
        align-items: center;
        flex: 1;
        margin-left: 20px;
        .board {
          display: flex;
          color: var(--mainText);
        }
        .nav-right {
          display: flex;
          .search {
          }
          .search-mobile {
            display: flex;
            align-items: center;
            font-size: 1.5em;
          }
          .user-operation {
            width: 200px;
            font-size: 0.5em;
            .theme-color {
              margin-left: 20px;
            }
            .user-info {
              display: flex;
              justify-content: space-around;
              align-items: center;
              .icon-xiaoxi {
                font-size: 1.2rem;
              }
            }
          }
        }
      }
    }
  }

  .category-tags-container {
    overflow: auto;
    .categories-list {
      align-items: center;
      display: flex;
      height: 48px;
      margin: 0 auto;

      .categories-list-item {
        color: #606a78;
        cursor: pointer;
        font-size: 15px;
        margin: 0 6px;
        padding: 3px 14px;
        word-break: keep-all;
      }
      .categories-active {
        background-color: #004fc4;
        border-radius: 3px;
        color: #fff;
        font-weight: 700;
      }
    }
  }
}
.body-content {
  margin-top: 20px;
}
.navbar-m {
  display: none;
}

@media (max-width: 800px) {
  .search {
    display: none;
  }
}
@media (min-width: 800px) {
  .search {
    display: flex;
    align-items: center;
  }
}
</style>
