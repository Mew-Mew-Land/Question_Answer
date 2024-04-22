<template>
  <div class="user-penal container-body">
    <div class="user-penal-left">
      <div class="user-base">
        <!-- 头像 -->
        <div class="avatar">
          <Avatar :width="120"></Avatar>
        </div>
        <!-- 用户名称 -->
        <h3>{{ userInfo.nickName }}</h3>
        <div class="user-data">
          <div class="data-item">
            <span class="iconfont icon-chengshi"></span>
            <span>{{ userInfo.city }}</span>
          </div>
          <div class="data-item">
            <span class="iconfont icon-shijian"></span>
            <span>{{ userInfo.createTime }}</span>
          </div>
          <div class="data-item">
            <span class="iconfont icon-gonggao"></span>
            <span>{{ userInfo.introduction }}</span>
          </div>
        </div>
        <div class="user-oper">
          <el-button
            v-if="userInfo.userId == store.loginUserInfo.userId"
            type="primary"
            style="background-color: var(--mainColor)"
            @click="dialogHandle()"
            >修改资料</el-button
          >
          <!-- <el-button type="primary">关注他</el-button>
          <el-button type="primary">发私信</el-button> -->
        </div>
        <div class="user-detail">
          <!-- <span>{{ loginUserInfo.city }}</span>
          <span>{{ loginUserInfo.summary }}</span>
          <span>{{ loginUserInfo.regTime }}加入</span>
          <span>{{ loginUserInfo.lastLoginTime }}上线</span> -->
        </div>
      </div>
    </div>
    <div class="user-penal-right">
      <el-tabs v-model="activeName" class="user-tabs" @tab-click="handleClick">
        <el-tab-pane label="提问" name="0"></el-tab-pane>
      </el-tabs>
      <el-skeleton v-if="!state" :rows="5" animated />
      <!-- <div class="datalist" v-else>
        <div class="list-item" v-for="item in userPostList.list">
          <div class="list-item-title">
            <RouterLink :to="`/faqDetail/${item.questionId}`" class="a-link">
              {{ item.title }}
            </RouterLink>
          </div>
          <div class="list-item-info">
            <span class="time">{{
              proxy.TransformIsoDate(item.createTime)
            }}</span>
            <div class="count">
              <span class="item-comment">{{ item.answerCount }}</span>
              |
              <span class="item-read">{{ item.viewCount }}</span>
            </div>
          </div>
        </div>
        <div class="pagination">
          <el-pagination
            background
            layout="prev, pager, next"
            v-if="userPostList.pagination.pages > 1"
            :total="userPostList.pagination.total"
            v-model:current-page="userPostList.pagination.page"
            @current-change="handelPageNoChange"
          />
        </div>
      </div> -->

      <div class="faq-list" v-else>
        <DataList :loading="loading" :dataSource="userPostList">
          <template #default="{ data }">
            <QuestionListItem :data="data"></QuestionListItem>
          </template>
        </DataList>
        <div class="pagination">
          <el-pagination
            background
            layout="prev, pager, next"
            :total="userPostList.pagination.total"
            v-model:current-page="userPostList.pagination.page"
            @current-change="handelPageNoChange"
          />
        </div>
      </div>
    </div>
  </div>

  <CommonDialog
    :title="dialogInfo.title"
    :width="dialogInfo.width"
    :buttons="dialogInfo.buttons"
    ref="editDialogRef"
  >
    <el-form :rules="rules" :model="userFormData" ref="userFormDataRef">
      <el-form-item label="昵称" prop="nickName">
        <el-input v-model="userFormData.nickName" />
      </el-form-item>
      <el-form-item label="城市" prop="city">
        <el-input v-model="userFormData.city" />
      </el-form-item>
      <el-form-item label="简介" prop="introduction">
        <el-input v-model="userFormData.introduction" type="textarea" />
      </el-form-item>
    </el-form>
  </CommonDialog>
</template>
<script setup>
import QuestionListItem from "../questionViews/QuestionListItem.vue";
import { onMounted, ref, watch, getCurrentInstance, reactive } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useMainStore } from "../../stores/index";
import { user, question } from "../../utils/api.utils";
const { proxy } = getCurrentInstance();
const store = useMainStore();
const route = useRoute();
const router = useRouter();
const state = ref(false);
const activeName = ref("0");
const loginUserInfo = ref({});

onMounted(() => {
  loginUserInfo.value = store.loginUserInfo;
});

// 用户信息
const userInfo = ref({});
const getUserInfo = async (userId) => {
  let result = await proxy.Request({
    url: user.findUserById + userId,
  });
  if (!result) return;
  result.data.createTime = proxy.TransformIsoDate(result.data.createTime);
  userInfo.value = result.data;
};

// 获取用户发表的文章
const userPostList = ref({});
const getPostByUser = async (type) => {
  // 获取问题列表
  if (type == 0) {
    let result = await proxy.Request({
      url: question.faqListByUser,
      params: {
        page: currentPage.value,
        pageSize: 10,
        userId: route.params.userId,
      },
    });
    if (!result) return;
    userPostList.value = result.data;

    state.value = true;
  } else {
  }
};

const currentPage = ref(1);
const handelPageNoChange = (pageNo) => {
  currentPage.value = pageNo;
  getPostByUser(activeName.value);
};

const editDialogRef = ref();
const dialogInfo = reactive({
  title: "修改资料",
  width: "500px",
  buttons: [
    {
      text: "保存",
      type: "primary",
      click: () => {
        submitForm();
      },
    },
  ],
});

const dialogHandle = () => {
  editDialogRef.value.showDialog();
};

// 修改用户资料表单校验
const userFormDataRef = ref();
const userFormData = ref({
  userId: store.loginUserInfo.userId,
  nickName: store.loginUserInfo.nickName,
  city: store.loginUserInfo.city,
  introduction: store.loginUserInfo.introduction,
});

const rules = reactive({
  nickName: [{ required: true, message: "请输入昵称", trigger: "blur" }],
  city: [{ required: true, message: "请输入城市", trigger: "blur" }],
  introduction: [{ required: true, message: "请输入简介", trigger: "blur" }],
});

const submitForm = () => {
  userFormDataRef.value.validate(async (valid) => {
    if (!valid) return;
    let result = await proxy.Request({
      url: user.updateUserInfo,
      params: userFormData.value,
    });
    if (!result) return;
    getUserInfo(store.loginUserInfo.userId);
    editDialogRef.value.handleClose();
  });
};

watch(
  () => route.params.userId,
  (newVal, oldVal) => {
    // console.log(newVal);
    getUserInfo(newVal);
  },
  { immediate: true, deep: true }
);

watch(
  () => activeName.value,
  (newVal, oldVal) => {
    // console.log(newVal);
    getPostByUser(newVal);
  },
  { immediate: true, deep: true }
);
</script>
<style lang="scss">
.user-penal {
  display: flex;
  .user-penal-left {
    .user-base {
      display: flex;
      flex-direction: column;
      justify-content: space-around;
      text-align: center;
      height: 500px;
      background-color: #fff;
      padding: 20px;
      .avatar {
      }

      .user-data {
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        .data-item {
          display: flex;
          justify-content: space-between;
          margin-top: 10px;
        }
      }
      .user-detail {
        width: 100%;
        padding-left: 2vw;
      }
      .user-detail {
        display: flex;
        flex-direction: column;
        margin-top: 10px;
        text-align: left;
      }
    }
    .user-extend-panel {
      margin-top: 10px;
      background-color: #fff;
      .info-item {
        display: flex;
        justify-content: space-between;
      }
    }
  }
  .user-penal-right {
    padding-left: 20px;
    padding-right: 20px;

    flex: 1;
    .user-tabs {
      background-color: #fff;
      padding-left: 10px;
    }
    .datalist {
      background-color: #fff;
      .list-nav {
        padding: 10px;
      }
      .list-item {
        padding: 10px;
        border-bottom: 1px #d3d3d3 solid;
        .list-item-info {
          display: flex;
          margin-top: 5px;
          .count {
            margin-left: 10px;
          }
        }
      }
    }
  }
}

@media (max-width: 800px) {
  .user-penal {
    flex-direction: column;
  }
  .user-base {
  }
  .user-penal-left {
    width: 100%;
  }
}
@media (min-width: 800px) {
  .user-penal-left {
    width: 300px;
  }
}
</style>
