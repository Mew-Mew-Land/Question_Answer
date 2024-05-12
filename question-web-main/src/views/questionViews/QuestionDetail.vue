<template>
  <div class="container-body question-detail">
    <el-skeleton v-if="!state" :rows="5" animated />
    <div class="question-detail-info" v-if="state">
      <div class="question-main">
        <Avatar></Avatar>


        <span class="author-name">
            <RouterLink
                class="a-link"
                :to="`/user/${questionDetail.user.userId}`"
            >
             作者: {{ questionDetail.user.nickName }}
            </RouterLink>
          </span>


        <el-divider direction="vertical" />

        <span>编辑于{{ proxy.TransformIsoDate(questionDetail.createTime) }}</span>

        <el-divider direction="vertical" />




        <h2 class="question-title">{{ questionDetail.title }}</h2>
<!--    <Avatar></Avatar>


          <span class="author-name">
            <RouterLink
              class="a-link"
              :to="`/user/${questionDetail.user.userId}`"
            >
              {{ questionDetail.user.nickName }}
            </RouterLink>
          </span>


          <el-divider direction="vertical" />

          <span>{{ proxy.TransformIsoDate(questionDetail.createTime) }}</span>

          <el-divider direction="vertical" />

          <div class="question-tags">{{ questionDetail.board.boardName }}</div>-->

        <!-- 正文 -->
        <div
          class="question-content"
          id="content"
          v-html="questionDetail.content"
        ></div>

        <div class="question-action">
          <!-- 当前当前题主操作 -->
          <el-button
            v-if="currentUserInfo.userId === questionDetail.user.userId"
            type="danger"
            @click="delQuestion()"
            >删除</el-button
          >
          <el-button
            v-if="currentUserInfo.userId === questionDetail.user.userId"
            type="primary"
            @click="editQuestion()"
            >编辑</el-button
          >
          <el-button
            type="primary"
            style="background-color: var(--mainColor)"
            @click="createAnswer()"
          >
            <span class="iconfont icon-wodewenzhang"></span>写回答
          </el-button>
        </div>
      </div>
      <!-- 回答列表 -->
      <el-skeleton v-if="!answerState" :rows="5" animated />
      <div class="question-answer-list" v-else>
        <div
          :class="['answer-item', item.isAdopt ? 'isAdopt' : '']"
          v-for="(item, index) in answerList.list"
        >
          <div class="answer-user-info">
            <Avatar></Avatar>
            <span class="answer-name">
              <RouterLink class="a-link" :to="`/user/${item.user.userId}`">
                {{ item.user.nickName }}
              </RouterLink>
            </span>
            <el-divider direction="vertical" />
            <span>{{ proxy.TransformIsoDate(item.createTime) }}</span>
          </div>
          <div v-if="item.isAdopt" class="answer-solvestate">
            <span class="iconfont icon-wancheng1">已被采纳</span>
          </div>
          <div class="answer-content" v-html="item.content"></div>

          <div class="answer-action">
            <el-button type="primary"><i class="iconfont icon-good"></i>点赞</el-button>
            <el-button type="primary"><i class="iconfont icon-huifu"></i>评论</el-button>
<!--            <span class="answer-reply" @click="commentHandle(index)">回复</span>-->
            <el-button
              v-if="currentUserInfo.userId === item.user.userId"
              type="primary"
              style="background-color: var(--mainColor)"
              @click="editAnswer(item)"
            >
              <i class="iconfont icon-bianji"></i>
              编辑
            </el-button>
            <el-button
              type="primary"
              v-if="
                currentUserInfo.userId === questionDetail.user.userId &&
                !questionDetail.isSolve
              "
              @click="adoptAnswer(item.answerId, index)"
            >
              <i class="iconfont icon-wancheng1"></i>
              采纳</el-button
            >
            <el-button
              v-if="currentUserInfo.userId === item.user.userId"
              type="danger"
              @click="delAnswer(item.answerId, index)"
            >
              <i class="iconfont icon-shanchu"></i>
              删除
            </el-button>
          </div>
          <CommentBox
            :answerId="item.answerId"
            v-if="item.showReply"
          ></CommentBox>



          <!-- 评论列表 -->
          <!-- <div class="reply-list">
            <div class="user-reply">

            </div>
            <div class="reply-action">
              <span>回复</span>
              <el-divider direction="vertical" />
              <span>4 月 14 日</span>
            </div>
          </div> -->
        </div>




<!--        底部-->
        <div class="pagination">
          <el-pagination
            background
            layout="prev, pager, next"
            v-if="answerList.pagination.pages > 1"
            :total="answerList.pagination.total"
            v-model:current-page="answerList.pagination.page"
            @current-change="handelPageNoChange"
          />
        </div>
      </div>
    </div>





<!--    侧边栏-->
    <div class="question-detail-sidebar">
      <el-card class="user-card" v-if="state">
        <template #header>
          <div class="card-header">
            <span>关于作者</span>
          </div>
        </template>
        <div class="author-info">
          <div class="infos">
            <div class="author-name">
<!--              <span>{{ questionDetail.user.nickName }}</span>-->
              <span>作者名称</span>
            </div>
          </div>
<!--          <div class="author-summary">-->
<!--            {{ questionDetail.user.introduction }}-->
<!--          </div>-->

                    <div class="author-summary">
                      这是作者的简介
                    </div>
        </div>
      </el-card>
<!--空白侧边栏-->
      <el-card class="user-card" v-if="state">
        <template #header>
          <div class="card-header">

          </div>
        </template>
        <div class="author-info">
          <div class="infos">
            <div class="author-name">
              <!--              <span>{{ questionDetail.user.nickName }}</span>-->

            </div>
          </div>
          <!--          <div class="author-summary">-->
          <!--            {{ questionDetail.user.introduction }}-->
          <!--          </div>-->


        </div>
      </el-card>


    </div>
  </div>
  <!-- 弹出框 -->
  <el-drawer v-model="drawer" :with-header="false" direction="btt" size="60%">
    <div class="answer-main">
      <el-form
        :rules="rules"
        :model="answerData"
        ref="answerDataRef"
        class="post-pannel"
        label-width="60px"
      >
        <el-card :body-style="{ padding: '0' }" style="box-shadow: none">
          <template #header>
            <div class="answer-title">
              <span>{{ questionDetail.title }}</span>
              <el-button
                type="primary"
                style="background-color: var(--mainColor)"
                @click="postAnswer()"
                >{{ editPostState ? "保存编辑" : "提交回答" }}</el-button
              >
            </div>
          </template>
          <el-form-item prop="content" label-width="0">
            <EditorMarkdown
              v-model="answerData.markdownContent"
              @htmlContent="setHtmlContent"
            ></EditorMarkdown>
          </el-form-item>
        </el-card>
      </el-form>
    </div>
  </el-drawer>
</template>
<script setup>
import CommentBox from "../../components/CommentBox.vue";
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
import { question, answer } from "../../utils/api.utils";
import { useMainStore } from "../../stores/index";
const store = useMainStore();
const currentUserInfo = ref({});

const { proxy } = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const drawer = ref(false);
const state = ref(false);
const answerState = ref(false);
// 文章详情
const questionDetail = ref({});
// 获取文章详情
const getQuestionDetail = async (questionId) => {
  let result = await proxy.Request({
    url: question.questionDetail,
    params: {
      questionId: questionId,
    },
  });
  if (!result) return;

  questionDetail.value = result.data;
  state.value = true;
  // console.log(questionDetail.value);
  highlightCode();
};

// 回答详情
const answerList = ref({});
const getAnswerList = async (questionId) => {
  let result = await proxy.Request({
    url: answer.getAnswerList,
    params: {
      page: currentPage.value,
      pageSize: 10,
      questionId: questionId,
    },
  });
  if (!result) return;
  answerList.value = result.data;
  answerState.value = true;
};
// 页数跳转
const currentPage = ref(1);
const handelPageNoChange = (pageNo) => {
  currentPage.value = pageNo;
  getAnswerList(route.params.questionId);
};

// 代码高亮;
const highlightCode = () => {
  nextTick(() => {
    let blocks = document.querySelectorAll("pre code");
    blocks.forEach((item) => {
      hljs.highlightBlock(item);
    });
  });
};

// 删除问题
const delQuestion = async () => {
  proxy.Confirm("确认删除？", () => {
    let result = proxy.Request({
      url: question.deleteFaq,
      params: {
        questionId: questionDetail.value.questionId,
      },
    });
    if (!result) {
      proxy.Message.error("删除失败");
      return;
    }
    proxy.Message.success("删除成功");
    router.push("/");
  });
};

// 编辑问题
const editQuestion = () => {
  router.push(`/editPost/${questionDetail.value.questionId}`);
};

// 回答校验

const rules = reactive({
  content: [{ required: true, message: "请输入正文", trigger: "blur" }],
});

// 创建回答
const createAnswer = () => {
  // console.log(currentUserInfo.value);
  if (!currentUserInfo.value) {
    proxy.Message.warning("请先登录");
    store.showLogin = 1;
    return;
  }
  drawer.value = true;
  editPostState.value = 0;
  answerData.value = {
    markdownContent: "",
  };
};

// 发送回答
const answerData = ref({});
const answerDataRef = ref();
const postAnswer = () => {
  let api = answer.createAnswer;
  if (editPostState.value) {
    api = answer.updateAnswer;
  } else {
    api = answer.createAnswer;
  }

  answerDataRef.value.validate(async (valid) => {
    if (!valid) {
      proxy.Message.warning("请输入内容");
      return;
    }
    let params = {};
    Object.assign(params, answerData.value);
    params.questionId = questionDetail.value.questionId;
    let result = await proxy.Request({
      url: api,
      params: params,
    });
    if (!result) return;
    proxy.Message.success("发送成功");
    getAnswerList(questionDetail.value.questionId);
    answerData.value.markdownContent = "";
    drawer.value = false;
  });
  // console.log(answerData.value);
};
const setHtmlContent = (htmlContent) => {
  answerData.value.content = htmlContent;
};

// 删除回答
const delAnswer = (answerId, index) => {
  proxy.Confirm("确认删除你的回答？", async () => {
    let result = await proxy.Request({
      url: answer.deleteAnswer,
      params: {
        answerId: answerId,
        questionId: questionDetail.value.questionId,
      },
    });
    if (!result) return;
    proxy.Message.success("删除成功");
    answerList.value.list.splice(index, 1);
  });
};

// 采纳回答
const adoptAnswer = (answerId, index) => {
  proxy.Confirm("确定采纳该回答？", async () => {
    let result = await proxy.Request({
      url: answer.adoptAnswer,
      params: {
        answerId: answerId,
        questionId: questionDetail.value.questionId,
      },
    });
    if (!result) return;
    proxy.Message.success("已采纳");
    answerList.value.list[index].isAdopt = true;
    questionDetail.value.isSolve = true;
  });
};

// 编辑回答
/**
 * editPostState,0表示发布，1表示编辑
 *
 *
 *
 */
const editPostState = ref(0);
const editAnswer = (item) => {
  editPostState.value = 1;
  drawer.value = true;
  // answerData.value = item;
  Object.assign(answerData.value, item);
};

// 处理回复框

const commentHandle = (index) => {
  answerList.value.list.forEach((element) => {
    element.showReply = false;
  });

  answerList.value.list[index].showReply = true;
};

// 获取评论操作
const getComment = async () => {};

watch(
  () => route.params.questionId,
  (newVal, oldVal) => {
    // getQuestionDetail(newVal);
    // getAnswerList(newVal);
    // currentUserInfo.value = store.loginUserInfo;
  },
  { immediate: true, deep: true }
);
onMounted(() => {
  getQuestionDetail(route.params.questionId);
  getAnswerList(route.params.questionId);
  currentUserInfo.value = store.loginUserInfo;
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

      .question-title {
      }
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
        // margin-bottom: 10px;
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
          .reply-action {
          }
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
    .user-card {
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
</style>
