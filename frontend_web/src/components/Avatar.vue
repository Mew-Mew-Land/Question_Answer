<!-- 头像 -->
<template>
  <el-avatar :size="width" :src="avatarUrl" />
</template>
<script setup>
const props = defineProps({
  width: {
    type: Number,
    default: 40,
  },
  avatarUrl: {
    type: String,
    default:
      'https://p9.itc.cn/images01/20231114/9d5c8a0d5bea4de1b6cb88c4e4d79af9.jpeg',
  },
});
</script>
<style></style>
