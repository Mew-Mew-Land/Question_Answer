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
        <el-card :body-style="{ padding: '5px' }" class="adaptive-card">
          <template #header>
            <div class="post-editor-title">
              <span
                  class="iconfont icon-fanhui"
                  style="cursor: pointer"
                  @click="back"
              >
                返回
              </span>
              <el-form-item class="input-main" prop="title">
                <el-input
                    class="input-title"
                    v-model="formData.title"
                    placeholder="请输入问题标题"
                />
              </el-form-item>
              <el-button
                  type="primary"
                  style="background-color: var(--mainColor)"
                  @click="isEdit ? updateQuestion() : post()"
              >
                {{ isEdit ? "编辑" : "发布" }}
              </el-button>
            </div>
          </template>
          <el-form-item label="分类" prop="boardId">
            <el-radio-group v-model="formData.classificationId" size="large">
              <el-radio-button
                  v-for="item in boardList"
                  :key="item.id"
                  :label="item.id"
              >
                {{ item.classification }}
              </el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item prop="content" label-width="0">
            <el-input
                type="textarea"
                v-model="formData.question"
                placeholder="请输入正文内容"
                :autosize="{ minRows: 15, maxRows: 20 }"
            />
          </el-form-item>
        </el-card>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import { ref, getCurrentInstance, watch, reactive } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useMainStore } from "../../stores/index";

const store = useMainStore();
const { proxy } = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const isEdit = ref(0);

const formData = ref({});
const formDataRef = ref();

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
  ],
  classificationId: [{ required: true, message: "请选择板块", trigger: "blur" }],
});

const editorType = ref(1);

const handleClose = () => {
  showDialog.value = false;
};

const boardList = ref([]);
const getBoardList = async () => {
  let result = await proxy.Request({
    url: "/board/all",
    params:{
    },
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

const post = () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) return;
    let params = {};
    Object.assign(params, formData.value);
    let result = await proxy.Request({
      url: "/home/submitQues",
      params: params,
    });
    if (!result) return;
    proxy.Message.success("发布成功");
    await router.push("/faqDetail/" + result.data.id);
  });
};

const questionId = ref(null);
const getQuestionDetail = async () => {
  let result = await proxy.Request({
    url: "/question/questionDetail",
    params: {
      id: questionId.value,
    },
  });
  if (!result) return;
  formData.value.title = result.data.title;
  formData.value.question = result.data.question;
  formData.value.id = result.data.id;
  formData.value.classificationId = result.data.classificationId;
};

const updateQuestion = () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) return;
    let params = {};
    Object.assign(params, formData.value);
    let result = await proxy.Request({
      url: "/home/submitQues/modify",
      params: params,
    });
    if (!result) return;
    proxy.Message.success("修改成功");
    await router.push("/faqDetail/" + formData.value.id);
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
  position: absolute;
  top: 50%;
  left: 50%;
  width: 70%;
  height: 100%;
  transform: translate(-50%, -50%);

  .post-pannel {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    .post-editor {
      width: 100%;
      background-color: rgba(255, 255, 255, 0.8);
      padding: 20px;
      border-radius: 10px;
      .adaptive-card {
        width: 100%;
        max-width: 1000px;
        margin: 0 auto;
      }
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
      }
    }
  }
}
.cust-dialog {
  max-height: 600px;
  overflow: auto;
}


</style>
