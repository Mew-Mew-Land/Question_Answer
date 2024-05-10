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
        <DataList :loading="loading" :dataSource="questionData">
          <template #default="{ data }">
            <QuestionListItem :data="data"></QuestionListItem>
          </template>
        </DataList>
<!--        问题下面的底部-->
<!--        <div class="pagination">-->
<!--          <el-pagination-->
<!--            background-->
<!--            layout="prev, pager, next"-->
<!--            v-if="questionData.pagination.pages > 1"-->
<!--            :total="questionData.pagination.total"-->
<!--            v-model:current-page="questionData.pagination.page"-->
<!--            @current-change="handelPageNoChange"-->
<!--          />-->
<!--        </div>-->
      </div>

      <!-- <div class="faq-list"></div> -->
    </div>
<!--    <div class="faq-side">-->
<!--      <div class="side-signin">-->
<!--        <div class="signin">-->
<!--          <span class="userInfo">👋Hi！ {{ userInfo.nickName }}</span>-->
<!--          &lt;!&ndash; <span class="time">{{ currentTime() + "好" }}</span> &ndash;&gt;-->
<!--        </div>-->
<!--        <div class="quote">这是一个技术问答平台，发表你的问题吧！👋</div>-->
<!--      </div>-->
<!--      <div class="side-acc">-->
<!--        <el-card class="box-card" style="box-shadow: none">-->
<!--          <template #header>-->
<!--            <div class="card-header">-->
<!--              <h3>公告</h3>-->
<!--            </div>-->
<!--          </template>-->
<!--          <span>暂无公告</span>-->
<!--        </el-card>-->
<!--      </div>-->
<!--    </div>-->
    <div class="faq-side">
      <!-- 创作中心区域 -->
      <el-card class="box-card" shadow="never">
        <template #header>
          <div class="clearfix">
            <span class="card-header">创作中心</span>
          </div>
        </template>
        <el-button-group>
          <el-button type="primary" icon="el-icon-edit">回答问题</el-button>
          <el-button type="primary" icon="el-icon-document">编写文章</el-button>
          <el-button type="primary" icon="el-icon-user">个人中心</el-button>
          <el-button type="primary" icon="el-icon-question">我的问题</el-button>
        </el-button-group>
      </el-card>

      <!-- 更多操作区域 -->
      <el-card class="box-card" style="margin-top: 10px;" shadow="never">
        <template #header>
          <div class="clearfix">
            <span class="card-header">更多操作</span>
          </div>
        </template>
        <div class="actions-container">
          <div class="action-item">
            <el-button type="info" icon="el-icon-view">浏览记录</el-button>
          </div>
          <div class="action-item">
            <el-button type="info" icon="el-icon-thumb">我的点赞</el-button>
          </div>
          <div class="action-item">
            <el-button type="info" icon="el-icon-star">我的收藏</el-button>
          </div>
          <div class="action-item">
            <el-button type="info" icon="el-icon-warning">我的举报</el-button>
          </div>
          <div class="action-item">
            <el-button type="info" icon="el-icon-wallet">我的余额</el-button>
          </div>
          <div class="action-item">
            <el-button type="info" icon="el-icon-setting">设置</el-button>
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
    url: question.faqList,
    params: {
      page: currentPage.value,
      pageSize: 10,
      boardId: boardId,
    },
    errorCallback: () => {
      proxy.Message.error("请求失败");
    },
  });
  if (!result) result;
  questionData.value = result.data;
  // 转换时间

  state.value = true;
  // console.log(questionData.list);
};
// getQuestionList();
const activeTab = ref("null");
const changeTab = (tab) => {
  activeTab.value = tab;
};

const currentPage = ref(1);
// 切换页
const handelPageNoChange = (pageNo) => {
  currentPage.value = pageNo;
  getQuestionList();
};

watch(
  () => route.params,
  (newVal, oldVal) => {
    console.log(newVal.boardId);
    if (newVal.boardId) {
      currentPage.value = 1;
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

.action-item .el-button {
  width: 230px; /* 设置按钮的宽度 */
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
