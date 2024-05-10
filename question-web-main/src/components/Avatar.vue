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
      "@/src/icon/5e9840c7_E882787_b31c866e.png",
  },
});
</script>
<style></style>
