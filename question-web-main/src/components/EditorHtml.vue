<!-- 富文本 -->
<template>
  <div class="editor-html">
    <Toolbar
      style="border-bottom: 1px solid #ccc"
      :editor="editorRef"
      :defaultConfig="toolbarConfig"
      :mode="mode"
    />
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
const editorRef = shallowRef();
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
