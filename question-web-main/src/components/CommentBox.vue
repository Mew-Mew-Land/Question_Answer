
<template>
  <div class="post-comment-panel">
    <div class="comment-form">

      <el-form :model="formData" :rules="rules" ref="formDataRef">

        <el-form-item prop="content">
<!--          :placeholder="placeholderInfo"：设置输入框的占位符文本。-->
<!--          resize="none"：输入框将保持固定的尺寸。-->
<!--          show-word-limit：显示输入框中字符的字数限制。-->
<!--          type="textarea"：指多行文本框。-->
          <el-input
            clearable
            :placeholder="placeholderInfo"
            :maxlength="150"
            resize="none"
            show-word-limit
            v-model="formData.content"
            type="textarea"
          />
        </el-form-item>
      </el-form>
    </div>
    <div class="send-button" @click="postCommentDo">发表</div>
  </div>
</template>

<script setup>
import { ref, getCurrentInstance } from "vue";
//像下面这种先创建实例再使用
import { comment } from "../utils/api.utils";
import { useMainStore } from "../stores/index";

const store = useMainStore();
//getCurrentInstance() 函数来获取当前正在执行的(子)组件实例,包含了组件实例的所有信息，包括组件的状态、生命周期钩子函数、props、事件等。
const { proxy } = getCurrentInstance();
const props = defineProps({
  answerId: {
    type: Number,
  },
  placeholderInfo: {
    type: String,
    default: "评论字数不少于5个",
  },
});

// form信息
// rules：表示校验规则的对象，包含了校验失败时的提示信息等。
// value：表示需要校验的值
// callback：是一个回调函数，用于通知校验结果。当校验成功时，不需要传递任何参数给 callback，当校验失败时，需要传递一个包含错误信息的参数给 callback
const checkPostComment = (rules, value, callback) => {
  if (value == null && formData.value.image == null) {
    callback(new Error(rules.message));
    return;
  }
  callback();
};

const formData = ref({});
const formDataRef = ref();
const rules = {
  content:  [
    { required: true,
      message: "请输入内容",
      //checkPostComment自动调用
      validator: checkPostComment },
    {
      min: 5,
      message: "评论最少五个字",
    },
  ],
};

const emit = defineEmits(["postCommentFinish"]);

// 发送评论
const postCommentDo = () => {
  if (!store.loginUserInfo) {
    proxy.Message.warning("请先登录");
    store.showLogin = 1;
    return;
  }
  //formDataRef.value 用于访问该引用所指向的实际表单元素对象
  //validate用rules验证,返回的是valid
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return;
    }
    //是用来创建一个参数对象 params，并将 formData 的值（即表单数据）复制给这个参数对象
    let params = Object.assign({}, formData.value);
    params.answerId = props.answerId;
    let result = await proxy.Request({
      url: comment.createComment,
      params,
    });
    if (!result) return;
    proxy.Message.success("发表成功");
    formDataRef.value.resetFields();
    // emit("postCommentFinish", result.data);
  });
};
</script>

<style lang="scss">
.post-comment-panel {
  display: flex; // 设置布局为弹性盒模型
  padding: 10px; // 内边距
  .comment-form {
    flex: 1; // 弹性布局，占满剩余空间
    margin-right: 10px; // 右侧外边距
    .el-textarea__inner {
      height: 60px; // 设置文本输入框的高度
    }
    .insert-img {
      margin-top: 10px; // 图片插入区域上边距
      line-height: normal; // 行高为正常
      .iconfont {
        margin-top: 3px; // 图标顶部外边距
        font-size: 20px; // 图标字体大小
        color: #636262; // 图标颜色
      }
      .pre-img {
        position: relative; // 定位方式为相对定位
        .iconfont {
          cursor: pointer; // 鼠标样式为指针
          position: absolute; // 绝对定位
          top: -10px; // 顶部偏移
          right: -10px; // 右侧偏移
          color: #727272; // 图标颜色
        }
      }
    }
  }
  .send-button {
    width: 60px; // 按钮宽度
    height: 60px; // 按钮高度
    background: var(--mainColor); // 背景颜色，使用 CSS 变量
    color: #fff; // 文字颜色
    text-align: center; // 文本居中
    line-height: 60px; // 行高
    border-radius: 5px; // 边框圆角
    cursor: pointer; // 鼠标样式为指针
  }
}
</style>
