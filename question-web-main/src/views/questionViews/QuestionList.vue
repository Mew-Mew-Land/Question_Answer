<template>
  <div class="container-body faq-list-body">
    <!-- 问题列表与右侧公告栏 -->
    <div class="faq-main">
      <el-tabs v-model="activeTab" class="faq-header" @tab-change="changeTab">
        <el-tab-pane label="按发布时间" name=""></el-tab-pane>
        <el-tab-pane label="按浏览次数" name="0"></el-tab-pane>
        <el-tab-pane label="按回答数量" name="1"></el-tab-pane>
      </el-tabs>

      <el-skeleton v-if="!state" :rows="5" animated />
      <div class="faq-list" v-else>
    <DataList  :dataSource="questionData">
      <template #default="{ data }">
        <QuestionListItem :data="data"></QuestionListItem>
      </template>
    </DataList>
  </div>
  </div>


    <div class="faq-side">
      <!-- 创作中心区域 -->
      <el-card class="box-card" shadow="never">
        <template #header>
          <div class="clearfix">
            <span class="card-header">创作中心</span>
          </div>
        </template>
        <el-button-group>
          <el-button type="info">
            <i class="iconfont icon-wancheng1"></i>回答问题
          </el-button>
          <el-button type="info">
            <i class="iconfont icon-bianjiwenzhang_huaban"></i>编写文章
          </el-button>
          <el-button type="info">
            <i class="iconfont icon-wodewenzhang"></i>个人中心
          </el-button>
          <el-button type="info">
            <i class="iconfont icon-gonggao"></i>我的问题
          </el-button>
        </el-button-group>
      </el-card>

      <!-- 更多操作区域 -->
      <el-card class="box-card2" style="margin-top: 10px;" shadow="never">
        <template #header>
          <div class="clearfix">
            <span class="card-header">更多操作</span>
          </div>
        </template>
        <div>
          <div class="action-item2">
            <el-button type="info">
              <i class="iconfont icon-shijian"></i>浏览记录
            </el-button>
          </div>
          <div class="action-item2">
            <el-button type="info">
              <i class="iconfont icon-good"></i> 我的点赞
            </el-button>
          </div>
          <div class="action-item2">
            <el-button type="info">
              <i class="iconfont icon-kong"></i> 我的收藏
            </el-button>
          </div>
          <div class="action-item2">
            <el-button type="info">
              <i class="iconfont icon-shanchu"></i> 我的举报
            </el-button>
          </div>
          <div class="action-item2">
            <el-button type="info">
              <i class="iconfont icon-gaojian-zuozhe"></i> 我的余额
            </el-button>
          </div>
          <div class="action-item2">
            <el-button type="info">
              <i class="iconfont icon-xiaoxi"></i> 系统设置
            </el-button>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { reactive, watch, ref, getCurrentInstance, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import QuestionListItem from "./QuestionListItem.vue";
import { question } from "../../utils/api.utils";
import { useMainStore } from "../../stores/index";

const store = useMainStore();
const route = useRoute();
const router = useRouter();
const { proxy } = getCurrentInstance();
const state = ref(false);
const userInfo = ref();

let loading = ref(false);
const questionData = ref({});

const getQuestionList = async (boardId) => {
  let result = await proxy.Request({
    url: "/question/faqList",
    params: {
      boardId: boardId,
    },
    errorCallback: () => {
      proxy.Message.error("请求失败");
    },
  });
  console.log(result);
  if (!result) return;
  questionData.value = result.data;
  state.value = true;
};

const activeTab = ref("null");
const changeTab = (tab) => {
  activeTab.value = tab;
};

watch(
    () => route.params,
    (newVal, oldVal) => {
      if (newVal.boardId) {
        getQuestionList(newVal.boardId);
        userInfo.value = store.loginUserInfo;
      } else {
        getQuestionList();
      }
    },
    { immediate: true, deep: true }
);

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

onMounted(() => {});
</script>

<style lang="scss">
.box-card {
  width: 270px;
  margin-left: 50px;
}

.box-card2 {
  width: 150px;
  margin-left: 50px;
  margin-top: 30px;
}
.action-item2 {
  margin: 10px; /* 为每个按钮添加间距 */
  display: flex; /* 使用Flexbox来对齐按钮 */
  justify-content: center; /* 按钮居中显示 */
}

.action-item2 .el-button {
  border: none; /* 移除按钮边框 */
  color: white; /* 设置文本颜色为白色 */
  font-weight: bold; /* 字体加粗 */
  padding: 10px 20px; /* 设置内边距 */
  border-radius: 5px; /* 设置圆角边 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
  transition: background-color 0.3s, box-shadow 0.3s; /* 过渡效果 */
  width: 100px;
}

.el-button:hover {
  background-color: var(--mainColor); /* 鼠标悬停时改变背景色 */
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2); /* 鼠标悬停时增加阴影 */
}

.action-item .el-button {
  width: 50px; /* 设置按钮的宽度 */
  height: 45px; /* 设置按钮的宽度 */
}
.faq-list-body {
  display: flex;
  min-height: 100vh;
  .faq-main {
    background-color: #ffffff;
    flex: 1;
    .faq-header {
      min-height: 54px;
      display: flex;
      align-items: center;
      line-height: 54px;
      padding-left: 15px;

      .is-active {
        color: var(--mainColor);
      }
    }
    .faq-list {
    }
  }
  .faq-side {
    width: 300px;
    margin-left: 10px;
    margin-top: 20px;
    text-align: center;

    .side-signin {
      display: flex;
      flex-direction: column;
      width: 100%;
      background-color: #ffffff;
      padding: 15px;
      margin-bottom: 1.2rem;
      .signin {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 10px;
        font-size: 1.2em;
        .userInfo {
        }
      }
      .quote {
        text-align: left;
      }
    }
    .side-acc {
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
      .box-card {
        width: 100%;
      }
    }
  }
}

@media (max-width: 800px) {
  .faq-list-item {
    flex-direction: column;
  }
  .read-num {
    flex-direction: row;
  }
  .faq-side {
    display: none;
  }
  .num-tips-container {
    justify-content: flex-end;
  }
}
@media (min-width: 800px) {
  .read-num {
    flex-direction: column;
  }
  .num-tips-container {
    justify-content: space-around;
  }
}
</style>
