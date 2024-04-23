<!-- 富文本 -->
<template>
  <div class="editor-html">
<!--    Toolbar 组件：负责提供富文本编辑器的工具栏，用户可以通过工具栏进行各种编辑操作，如加粗、斜体、插入链接等-->
    <Toolbar
      style="border-bottom: 1px solid #ccc"
      :editor="editorRef"
      :defaultConfig="toolbarConfig"
      :mode="mode"
    />

<!--    Editor 组件：实际的富文本编辑器，用户在这里输入和编辑文本内容-->
<!--    style根据组件的 height 属性动态设置编辑器的高度，并隐藏垂直滚动条。-->
<!--    onCreated 事件，当编辑器创建完成时调用 handleCreated 方法进行处理。-->
<!--    onChange 事件，当编辑器的内容发生变化时调用 onChange 方法进行处理。-->
    <Editor
      :style="{ height: height + 'px', 'overflow-y': 'hidden' }"
      v-model="valueHtml"
      :defaultConfig="editorConfig"
      :mode="mode"
      @onCreated="handleCreated"
      @onChange="onChange"
    />
  </div>
</template>

<script setup>
//以下是这个编辑器组件介绍:https://www.wangeditor.com/v5/for-frame.html#%E4%BD%BF%E7%94%A8-1
import "@wangeditor/editor/dist/css/style.css";
import { onBeforeMount, ref, shallowRef, getCurrentInstance } from "vue";
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";

const { proxy } = getCurrentInstance();

const props = defineProps({
  modelValue: {
    type: String,
    default: "",
  },
  height: {
    type: Number,
    default: 500,
  },
});

const mode = ref("default");
// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef();
// 内容 HTML
const valueHtml = ref('<p>hello</p>')

const toolbarConfig = {
  // 排除菜单组，写菜单组的key的值即可
  excludeKeys: ["uploadVideo"],
};

const editorConfig = {
  placeholder: "请输入内容",
  excludeKeys: ["uploadVideo"],
  MENU_CONF: {
    // uploadImage: {
    //   maxFileSize: 3 * 1024 * 1024,
    //   server: "/api/file/uploadImage",
    //   fieldName: "file",
    //   customInsert(responseData, insertFn) {
    //     // 正常请求
    //     if (responseData == 200) {
    //       insertFn(proxy.globalInfo.imageUrl + responseData.data.fileName),
    //         "",
    //         "";
    //       return;
    //     } else if (responseData == 901) {
    //       // 登陆超时
    //       store.commit("showLogin", true);
    //       store.commit("updateLoginUserInfo", null);
    //       return;
    //     }
    //     proxy.Message.error(responseData.info);
    //   },
    // },
  },
};

const emit = defineEmits();
// onChange 函数中，通过 emit 函数触发了一个名为 update:modelValue 的事件，并传递了编辑器的 HTML 内容作为参数
const onChange = (editor) => {
  emit("update: modelValue", editor.getHtml());
};

// 组件销毁时，也及时销毁编辑器
onBeforeMount(() => {
  const editor = editorRef.value;
  if (editor == null) return;
  editor.destroy();
});

const handleCreated = (editor) => {
  // 记录editor实例
  editorRef.value = editor;
};
</script>
<style lang="scss">
.editor-html {
  width: 100%;
  margin: auto;
  border: 1px solid #ddd;
}
</style>
