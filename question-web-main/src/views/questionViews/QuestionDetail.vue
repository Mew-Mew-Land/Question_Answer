<template>
  <div class="container-body question-detail">
    <!-- 数据加载时的骨架屏 -->
<!--    <el-skeleton v-if="!state" :rows="5" animated />-->

    <!-- 问题详情部分 -->
    <div class="question-detail-info" v-if="state">

      <div class="question-main">
        <!-- 问题作者信息 -->
        <Avatar></Avatar>
        <span class="author-name">
          <RouterLink
              class="a-link"
              :to="`/user/${questionDetail.userId}`"
          >
            作者: {{ questionDetail.userId }}
          </RouterLink>
        </span>
        <el-divider direction="vertical" />
        <span>编辑于{{ questionDetail.updateTime }}</span>
        <el-divider direction="vertical" />
        <h2 class="question-title">{{ questionDetail.question }}</h2>

        <!-- 问题内容 -->
        <div
            class="question-content"
            id="content"
            v-html="questionDetail.question"
        ></div>

        <!-- 问题操作按钮 -->
<!--        v-if="currentUserInfo.userId === questionDetail.userId"-->
        <div class="question-action">
          <el-button

              type="danger"
              @click="delQuestion()"
          >删除</el-button>
<!--          v-if="currentUserInfo.userId === questionDetail.user.userId"-->
          <el-button

              type="primary"
              @click="editQuestion()"
          >编辑</el-button>
          <el-button
              type="primary"
              style="background-color: var(--mainColor)"
              @click="createAnswer()"
          >
            <span class="iconfont icon-wodewenzhang"></span>写回答
          </el-button>
        </div>
      </div>

      <!-- 问题回答列表 -->
      <div class="question-answer-list">
        <div
            class="answer-item"
            v-for="(item, index) in answerList"
        >
          <!-- 回答者信息 -->
          <div class="answer-user-info">
            <Avatar></Avatar>
            <span class="answer-name">
              <RouterLink class="a-link" :to="`/user/${item.userId}`">
                {{ item.username }}
              </RouterLink>
            </span>
            <el-divider direction="vertical" />
            <span>{{ item.updateTime }}</span>
          </div>


          <!-- 回答内容 -->
          <div class="answer-content" v-html="item.content"></div>

          <!-- 回答操作按钮 -->
          <div class="answer-action">
            <el-button type="primary"><i class="iconfont icon-good"></i>点赞</el-button>
            <el-button type="primary"><i class="iconfont icon-huifu"></i>评论</el-button>

            <el-button
                        v-if="currentUserInfo.userId === item.userId"
                type="primary"
                style="background-color: var(--mainColor)"
                @click="editAnswer(item)"
            >
              <i class="iconfont icon-bianji"></i>编辑
            </el-button>

            <el-button
                type="primary"

                @click="adoptAnswer(item.answerId, index)"
            >
              <i class="iconfont icon-wancheng1"></i>采纳
            </el-button>

            <el-button
                        v-if="currentUserInfo.userId === item.userId"
                type="danger"
                @click="delAnswer(item.answerId, index)"
            >
              <i class="iconfont icon-shanchu"></i>删除
            </el-button>
          </div>


        </div>

        <!-- 评论部分 -->
        <div class="comments-section" v-if="state">
          <div class="comment-list">
            <div class="comment-item" v-for="(comment, index) in comments" :key="index">
              <div class="comment-user">
                <Avatar></Avatar>
                <span class="comment-name">{{ comment.user }}</span>
              </div>
              <div class="comment-content" v-html="comment.text"></div>
              <div class="comment-action">
                <el-button @click="replyToComment(index)">回复</el-button>
                <span class="comment-date">{{ comment.date }}</span>
              </div>
            </div>
          </div>

          <!-- 新评论输入框 -->
          <div class="new-comment">
            <el-form :rules="rules" :model="answerData" ref="answerDataRef" @submit.native.prevent="postAnswer">
              <el-form-item prop="content" label="评论内容">
                <el-input
                    type="textarea"
                    v-model="answerData.content"
                    :autosize="{ minRows: 2, maxRows: 4 }"
                    placeholder="请输入内容"
                ></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="postAnswer">提交评论</el-button>
              </el-form-item>
            </el-form>
          </div>


        </div>
      </div>
    </div>

    <!-- 问题详情侧边栏 -->
    <div class="question-detail-sidebar">
      <!-- 关于作者部分 -->
      <el-card class="user-card" v-if="state">
        <template #header>
          <div class="card-header">
            <span>关于作者</span>
          </div>
        </template>
        <div class="author-info">
          <div class="infos">
            <div class="author-name">
              <span>作者名称</span>
            </div>
          </div>
          <div class="author-summary">
            这是作者的简介
          </div>
        </div>
      </el-card>

      <!-- 其他信息部分 -->
      <el-card class="user-card" v-if="state">
        <template #header>
          <div class="card-header"></div>
        </template>
        <div class="author-info">
          <div class="infos">
            <div class="author-name"></div>
          </div>
        </div>
      </el-card>
    </div>
  </div>

  <!-- 回答编辑抽屉 -->

</template>

<script setup>
// 引入必要的模块和库
import {
  ref,
  watch,
  getCurrentInstance,
  nextTick,
  onMounted,
  reactive,
} from "vue";
import { useRoute, useRouter } from "vue-router";
import hljs from "highlight.js";
import "highlight.js/styles/atom-one-light.css";

import { useMainStore } from "../../stores/index";


const store = useMainStore();
const currentUserInfo = ref({});

// 获取当前实例的代理
const { proxy } = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const drawer = ref(false);
const state = ref(false);
const answerState = ref(false);

// 初始评论数据
const comments = ref([
  { user: '用户A', text: '这是一条评论', date: '2024-05-19' },
]);

// 新建评论表单数据
const newComment = ref({
  text: '',
});

// 提交评论的方法
const submitComment = () => {
  console.log('提交评论:', newComment.value.text);
  newComment.value.text = '';
};

// 回复评论的方法
const replyToComment = (index) => {
  console.log(`回复评论 ${index}:`, comments.value[index].text);
};

const questionDetail = ref({});
const getQuestionDetail = async (questionId) => {

  //返回的data
  // {
  //   "id": 3,
  //     "question": "111",
  //     "classificationId": 2,
  //     "answerNum": 0,
  //     "isSolved": 0,
  //     "updateTime": "2024-05-29",
  //     "userId": 0,
  //     "username": null,
  //     "viewNum": 0
  // }
  let result = await proxy.Request({
    url: "/question/questionDetail",
    params: {
      id: questionId,
    },
  });
  if (!result) return;

  questionDetail.value = result.data;
  console.log(questionDetail);
  state.value = true;
};

const answerList = ref({});
const getAnswerList = async (questionId) => {
  //返回的data
  // {
  //   "id": 1,
  //     "question": "woshishei",
  //     "classificationId": 1,
  //     "answerNum": 0,
  //     "isSolved": 0,
  //     "updateTime": "2024-05-28",
  //     "userId": 0,
  //     "username": null,
  //     "viewNum": 0
  // }
  let result = await proxy.Request({
    url: "/home/viewAnswers",
    params: {
        id: questionId,
    },
  });
  if (!result) return;
  answerList.value = result.data;
  answerState.value = true;
};

const delQuestion = async () => {

    let result = proxy.Request({
      url: "/deleteQuestion",
      params: {
        id: questionDetail.value.id,
      },
    });
if(result.code==0){
  proxy.Message.success("这不是你的问题");
}
    proxy.Message.success("删除成功");
    await router.push("/");

  ;
};

const editQuestion = () => {
  router.push(`/editPost/${questionDetail.value.id}`);
};

const rules = reactive({
  content: [{ required: true, message: "请输入正文", trigger: "blur" }],
});

// 创建回答的方法
const createAnswer = () => {
  if (!currentUserInfo.value) {
    proxy.Message.warning("请先登录");
    store.showLogin = 1;
    return;
  }
  drawer.value = true;
  answerData.value = {
    content: "",
  };
};

const answerData = ref({});
const answerDataRef = ref();
const postAnswer = () => {
  let api =  "/AnswerQues";

  answerDataRef.value.validate(async (valid) => {
    if (!valid) {
      proxy.Message.warning("请输入内容");
      return;
    }
    let params = {};
    //接口
    Object.assign(params, answerData.value);
    params.questionId = questionDetail.value.id;
    let result = await proxy.Request({
      url: api,
      params: params,
    });

    if (!result) return;
    proxy.Message.success("发送成功");
    await getAnswerList(questionDetail.value.id);
    answerData.value.markdownContent = "";
    drawer.value = false;
  });
};

// 设置HTML内容
const setHtmlContent = (htmlContent) => {
  answerData.value.content = htmlContent;
};

// 删除回答的方法
const delAnswer = async (answerId, index) => {

  let result = await proxy.Request({
    url: "/deleteAnswer",
    params: {
      id: answerId,
      questionId: questionDetail.value.id,
    },
  });
  if (!result) return;
  proxy.Message.success("删除成功");

};




// 获取评论的方法
const getComment = async () => {};

onMounted(() => {//432
  getQuestionDetail(route.params.questionId);
  getAnswerList(route.params.questionId);
  //currentUserInfo.value = store.loginUserInfo;
});
</script>


<style lang="scss">
.question-detail {
  display: flex;

  .question-detail-info {
    flex: 1;
    .question-main {
      background-color: #fff;
      padding: 25px;
      opacity: 0.9;

      .question-title {}
      .author-info {
        display: flex;
        align-items: center;
        margin-top: 20px;

        .author-name {
          margin-left: 10px;
        }
      }
      .question-content {
        margin-top: 10px;
      }
      .question-action {
        padding: 1vw 0;
      }
    }
    .question-answer-list {
      margin-top: 20px;
      .answer-item {
        border-bottom: 1px solid #c1c1c1;
        background-color: #fff;
        padding: 25px;

        .answer-user-info {
          display: flex;
          align-items: center;
          margin-bottom: 10px;
          .answer-name {
            margin-left: 10px;
          }
        }
        .answer-solvestate {
          span {
            border-radius: 10px;
            padding: 5px;
            background-color: var(--mainColor);
            font-size: 0.5em;
            color: #fff;
          }
        }
        .answer-content {
          margin-top: 20px;
        }
        .answer-action {
          padding: 1vw 0;
          .answer-reply {
            margin-right: 10px;
            cursor: pointer;
          }
        }
        .reply-list {
          background-color: #e9ecef;
          padding: 10px;
          .user-reply {
            margin-bottom: 5px;
          }
          .reply-action {}
        }
      }
      .isAdopt {
        border: 2px solid var(--mainColor);
      }
    }
  }
  .question-detail-sidebar {
    width: 305px;
    margin-left: 15px;
    opacity: 0.9;
    .user-card {
      margin-left: 0px;
      margin-top: 20px;
      box-shadow: none;
      .author-info {
        .infos {
          display: flex;
          justify-content: space-between;

          .author-name {
            font-size: 1.2em;
            margin-bottom: 15px;
          }
        }
      }
    }
  }
}

.answer-main {
  background-color: #f7f8fa;
  .answer-title {
    display: flex;
    justify-content: space-between;
  }
}

@media (min-width: 800px) {
  .question-detail-sidebar {
    display: block;
  }
}

@media (max-width: 800px) {
  .question-detail-sidebar {
    display: none;
  }
}

.comments-section {
  background-color: #fff;
  padding: 25px;
  margin-top: 20px;
  opacity: 0.9;
  .comment-list {
    .comment-item {
      border-bottom: 1px solid #c1c1c1;
      padding-bottom: 15px;
      margin-bottom: 15px;
      .comment-user {
        display: flex;
        align-items: center;
        margin-bottom: 10px;
      }
      .comment-content {
        width: 300px; // 确保评论内容有足够的显示空间
        min-height: 50px; // 增加最小高度以确保可见性
      }
      .comment-action {
        display: flex;
        align-items: center;
        justify-content: space-between;
      }
    }
  }
  .new-comment {
    margin-top: 20px;
    el-input {
      min-height: 100px; // 设置输入框的最小高度
      width: 100%; // 设置输入框宽度为100%，以填满容器
      border: 1px solid #c1c1c1; // 添加边框以增强可见性
      padding: 10px; // 增加内边距
    }
  }
}
</style>

