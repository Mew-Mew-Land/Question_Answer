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
                  style="background-color: var(--mainColor)"
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
  <el-dialog
      v-model="showDialog"
      :title="dialogInfo.title"
      custom-class="cust-dialog"
      :width="dialogInfo.width"
      @close="handleClose"
  >
    <div class="dialog-body">
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
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button
            v-for="btn in dialogInfo.buttons"
            :type="btn.type"
            @click="btn.click"
        >
          {{ btn.text }}
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, getCurrentInstance, watch, reactive } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useMainStore } from "../../stores/index";
import { board, question } from "../../utils/api.utils";

const store = useMainStore();
const { proxy } = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const isEdit = ref(0);

const formData = ref({});
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
          updateQuestion();
        } else {
          postHandler();
        }
      },
    },
  ],
});

const showDialog = ref(false);

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

const editorType = ref(1);

const changeEditor = () => {
  editorType.value = editorType.value == 0 ? 1 : 0;
  formData.value.content = "";
  formData.value.markdownContent = "";
};

const handleClose = () => {
  showDialog.value = false;
};

const dialogHandle = () => {
  showDialog.value = true;
};

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
    await router.push("/faqDetail/" + result.data.result.questionId);
  });
};

const questionId = ref(null);
const getQuestionDetail = async () => {
  let result = await proxy.Request({
    url: question.questionDetail,
    params: {
      questionId: questionId.value,
    },
  });
  if (!result) return;
  formData.value.title = result.data.title;
  formData.value.content = result.data.content;
  formData.value.markdownContent = result.data.markdownContent;
  formData.value.questionId = result.data.questionId;
  formData.value.boardId = result.data.board.boardId;
};

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
.cust-dialog {
  max-height: 600px;
  overflow: auto;
}
</style>
