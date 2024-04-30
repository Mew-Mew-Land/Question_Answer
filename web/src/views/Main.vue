
  <template>
    <div class="header">
      <div class="header-container">
        <div class="header-body">
          <div class="logo">
            <img src="@/assets/vue.svg" alt="" srcset="" />
          </div>
<!--LOGO字体导航-->
          <div class="navbar">
            <div class="board">
              <RouterLink to="/" class="nav-link">问答</RouterLink>
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

        </div>

    </div>
    </div>

  </template>


  <script setup>
  //import LoginReg from "../components/LoginReg.vue";
  import { ref, watch, getCurrentInstance, onMounted } from "vue";
  import { useRoute, useRouter } from "vue-router";
  import { useMainStore } from "../stores/index";
  //import { board } from "../utils/api.utils";
  const { proxy } = getCurrentInstance();
  const route = useRoute();
  const router = useRouter();
  const store = useMainStore();

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
  </script>

  <style lang="scss" scoped>
  .header {
    width: 100%;
    background-color: #fff;
    position: sticky;//创建固定在页面顶部或底部的导航栏或工具栏
    top: 0;
    left: 0;
   z-index: 999;//在其他元素上面
    .header-container {
      width: 100%;
      border-bottom: #efefef 1px solid;//底部边框的样式
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
