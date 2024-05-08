<template>
  <div class="edit-post">
    <el-form
        :model="formData"
        :rules="rules"
        ref="formDataRef"
        class="post-pannel"
        label-width="60px"
    >
      <div class="post-editor">
        <el-card :body-style="{ padding: '5px' }" style="box-shadow: none">
          <template #header>
            <div class="post-editor-title">
              <span
                  class="iconfont icon-fanhui"
                  style="cursor: pointer"
                  @click="back"
              >
                返回</span
              >
              <el-form-item class="input-main" prop="title">
                <el-input
                    class="input-title"
                    v-model="formData.title"
                    placeholder="请输入问题标题，文本支持markdown语法"
                />
              </el-form-item>

              <el-button
                  type="primary"

                  @click="dialogHandle()"
              >发布</el-button
              >
            </div>
          </template>
          <el-form-item prop="content" label-width="0">
            <!-- markdown编辑器 -->
            <EditorMarkdown
                v-if="editorType == 1"
                :height="700"
                v-model="formData.markdownContent"
                @htmlContent="setHtmlContent"
            ></EditorMarkdown>
          </el-form-item>
        </el-card>
      </div>
    </el-form>
  </div>
  <CommonDialog
      :title="dialogInfo.title"
      :width="dialogInfo.width"
      :buttons="dialogInfo.buttons"
      ref="postDialogRef"
  >
    <el-form :rules="rules">
      <el-form-item label="选择分类" prop="boardId">
        <el-radio-group v-model="formData.boardId" size="large">
          <el-radio-button
              v-for="item in boardList"
              :key="item.boardId"
              :label="item.boardId"
          >
            {{ item.boardName }}
          </el-radio-button>
        </el-radio-group>
      </el-form-item>
    </el-form>
  </CommonDialog>
</template>
<script setup>
import { ref, getCurrentInstance, watch, reactive } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useMainStore } from "../../stores/index";
import { board, question } from "../../utils/api.utils";



const { proxy } = getCurrentInstance();
const store = useMainStore();
const router = useRouter();
const route = useRoute();

// 当前状态 发布0 / 编辑1
const isEdit = ref(0);

// 选中的板块

const formData = ref({
  // questionId: "",
  // title: "",
  // markdownContent: "",
  // content: "",
  // boardId: chooseBoard.value,
});
const formDataRef = ref();

const dialogInfo = reactive({
  title: isEdit.value ? "编辑" : "发布",
  width: "45%",
  buttons: [
    {
      text: isEdit.value ? "保存" : "发布",
      type: "primary",
      click: () => {
        if (isEdit.value) {
          // 发布编辑的文章
          updateQuestion();
        } else {
          postHandler();
        }
      },
    },
  ],
});

const back = () => {
  router.go(-1);
};

const rules = reactive({
  title: [
    {
      required: true,
      message: "请输入标题",
    },
    {
      max: 150,
      message: "标题太长",
    },
  ],
  boardId: [{ required: true, message: "请选择板块", trigger: "blur" }],
  content: [{ required: true, message: "请输入正文", trigger: "blur" }],
});

// 编辑器类型 0:富文本 1:markdown
const editorType = ref(1);

const changeEditor = () => {
  editorType.value = editorType.value == 0 ? 1 : 0;
  formData.value.content = "";
  formData.value.markdownContent = "";
};
const postDialogRef = ref();
const dialogHandle = () => {
  postDialogRef.value.showDialog();
};
// 单选框

const boardList = ref([]);
const getBoardList = async () => {
  let result = await proxy.Request({
    url: board.allBoard,
    errorCallback: () => {
      proxy.Message.error("查询板块失败");
    },
  });
  if (!result) return;
  boardList.value = result.data;
};

const setHtmlContent = (htmlContent) => {
  formData.value.content = htmlContent;
};

const postHandler = () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) return;
    let params = {};
    Object.assign(params, formData.value);
    let result = await proxy.Request({
      url: question.createFaq,
      params: params,
    });
    if (!result) return;
    proxy.Message.success("发布成功");
    router.push("/faqDetail/" + result.data.result.questionId);
  });
};

// 获取问题信息
const questionId = ref(null);
const getQuestionDetail = async () => {
  let result = await proxy.Request({
    url: question.questionDetail,
    params: {
      questionId: questionId.value,
    },
  });
  if (!result) return;
  // console.log(result.data);
  formData.value.title = result.data.title;
  formData.value.content = result.data.content;
  formData.value.markdownContent = result.data.markdownContent;
  formData.value.questionId = result.data.questionId;
  formData.value.boardId = result.data.board.boardId;
};

// 保存修改的问题信息
const updateQuestion = () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) return;
    let params = {};
    Object.assign(params, formData.value);
    let result = await proxy.Request({
      url: question.updateFaq,
      params: params,
    });
    if (!result) return;
    proxy.Message.success("修改成功");
    router.push("/faqDetail/" + formData.value.questionId);
  });
};

watch(
    () => route,
    (newVal, oldVal) => {
      if (newVal.path.indexOf("/editPost") != -1) {
        isEdit.value = 1;
        questionId.value = newVal.params.questionId;
        getQuestionDetail();
      } else {
      }
      getBoardList();
    },
    { immediate: true, deep: true }
);
</script>
<style lang="scss">
.edit-post {
  overflow: auto;
  .post-pannel {
    display: flex;
    height: 100vh;
    .post-editor {
      flex: 1;
      .post-editor-title {
        display: flex;
        justify-content: space-between;
        align-items: center;
        .input-main {
          display: flex;
          align-items: center;
          flex: 1;
          .input-title {
            margin-right: 60px;
          }
        }

        .change-editor-type {
          .iconfont {
            cursor: pointer;
            color: var(--link);
            font-size: 14px;
          }
        }
      }
    }
    .post-setting {
      margin-left: 10px;
      margin-right: 10px;
      width: 400px;
      .setting-inner {
        max-height: calc(100vh - 120px);
        overflow: auto;
        .el-form-item {
          align-items: flex-start;
        }
      }
      .tips {
        color: #ddd;
        font-size: 13px;
      }
    }
  }
}
</style>
