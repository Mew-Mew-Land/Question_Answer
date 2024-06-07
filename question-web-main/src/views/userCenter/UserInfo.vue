<template>
  <div class="user-penal container-body">
    <div class="user-penal-left">
      <div class="user-base">
        <!-- 头像 -->
        <div class="avatar">
          <Avatar :width="120"></Avatar>
        </div>
        <!-- 用户名称 -->
        <h3>{{ userInfo.accountName }}</h3>
        <div class="user-data">
          <div class="data-item">
            <span class="iconfont icon-chengshi"></span>
            <span>其他信息</span>
          </div>
          <div class="data-item">
            <span class="iconfont icon-shijian"></span>
            <span>{{ userInfo.createTime }}</span>
          </div>
          <div class="data-item">
            <span class="iconfont icon-gonggao"></span>
            <span>用户简介</span>
          </div>
        </div>
        <div class="user-oper">
          <el-button
            type="primary"
            style="background-color: var(--mainColor)"

            >修改资料</el-button
          >
          <el-button type="primary">关注他</el-button>
          <el-button type="primary">发私信</el-button>
        </div>

      </div>
    </div>
    <div class="user-penal-right">
      <el-tabs v-model="activeName" class="user-tabs">
        <el-tab-pane label="提问" name="0"></el-tab-pane>
      </el-tabs>

      <div class="faq-list" >
        <div class="datalist">
          <div class="list-item" v-for="item in userPostList">

            <div class="faq-list-item">
              <div class="num-tips-container">
                <div class="read-num">
                  <img src="@/icon/9fcef5305e65ab4288ff910a7fbb1696.png" alt="Description of the image" />
                </div>
              </div>
              <div class="question-item">
                <div class="title">
                  <RouterLink :to="`/faqDetail/${item.id}`" class="a-link">
                    {{ item.question }}
                  </RouterLink>
                </div>
                <div class="userInfo">
                  <div class="author">
                    作者名
                    <el-divider direction="vertical"></el-divider>
                    {{ item.question }}
                  </div>
                  <span class="board">
                   <RouterLink :to="`/faqDetail/${item.id}`" class="a-link">
                    查看详情
                  </RouterLink>
                </span>
                </div>
              </div>
            </div>
          </div>

        </div>
      </div>
    </div>
  </div>

</template>
<script setup>

import { onMounted, ref, watch, getCurrentInstance, reactive } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useMainStore } from "../../stores/index";
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
  //{
//   "id": 1,
//       "username": "111",
//       "password": null,
//       "accountName": "323232",
//       "avatar": 0
// }
  let result = await proxy.Request({
    url: "/account/Info",
    params:{
      userId:userId,
    }
  });
  if (!result) return;
  userInfo.value = result.data;
};

// 获取用户发表的文章
const userPostList = ref({});
const getPostByUser = async (type) => {
  // 获取问题列表,返回的data数据
  //[
//   {
//     "id": 9,
//       "question": "woshumo",
//       "classificationId": 0,
//       "answerNum": 0,
//       "isSolved": 0,
//       "updateTime": "2024-06-01",
//       "userId": 0,
//       "username": null,
//       "viewNum": 0
//   },
//   {
//     "id": 10,
//       "question": "你是?",
//       "classificationId": 1,
//       "answerNum": 0,
//       "isSolved": 0,
//       "updateTime": "2024-06-01",
//       "userId": 0,
//       "username": null,
//       "viewNum": 0
//   },
//   {
//     "id": 11,
//       "question": "测试",
//       "classificationId": 1,
//       "answerNum": 0,
//       "isSolved": 0,
//       "updateTime": "2024-06-01",
//       "userId": 0,
//       "username": null,
//       "viewNum": 0
//   }
// ]
  if (type == 0) {
    let result = await proxy.Request({
      url: "/QuesListByUser",
      params: {
        userId: route.params.userId,
      },
    });
    if (!result) return;
    userPostList.value = result.data;

    state.value = true;
  } else {
  }
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
      url: "/updateUserInfo",
      params: userFormData.value,
    });
    if (!result) return;
    await getUserInfo(store.loginUserInfo.userId);
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
