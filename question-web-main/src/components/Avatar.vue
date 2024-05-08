<!-- 头像 -->
<template>
  <el-avatar :size="width" :src="avatarUrl" />
</template>
<script setup>
//defineProps定义的数据自动载入到<template>中的定义
const props = defineProps({
  width: {
    type: Number,
    default: 40,
  },
  avatarUrl: {
    type: String,
    default:
      "",
  },
});
</script>
<style></style>
