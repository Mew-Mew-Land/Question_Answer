<template>
  <!-- 点击发布按钮时显示的对话框 -->
  <el-dialog
      v-model="show"
      :title="title"
      custom-class="cust-dialog"
      :width="width"
      @close="handleClose"
  >
    <div class="dialog-body">
      <slot></slot>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button v-for="btn in buttons" :type="btn.type" @click="btn.click">
          {{ btn.text }}</el-button
        >
      </span>
    </template>
  </el-dialog>
</template>
<script setup>
import { onMounted, ref } from "vue";
const props = defineProps({
  title: {
    type: String,
    default: "标题",
  },
  showClose: {
    type: Boolean,
    default: true,
  },
  width: {
    type: String,
    default: "400px",
  },
  buttons: {
    type: Array,
  },
});
const show = ref(false);
const showDialog = () => {
  show.value = true;
};

const handleClose = () => {
  show.value = false;
};
defineExpose({
  showDialog,
  handleClose,
});
</script>
<style lang="scss">
.cust-dialog {
  max-height: 600px;
  overflow: auto;
}
</style>
