<template>
  <div class="message-body container-body">
    <el-tabs v-model="activeName" class="user-tabs" @tab-click="handleClick">
      <el-tab-pane label="回复我的" name="answer"></el-tab-pane>
      <el-tab-pane label="点赞" name="question"></el-tab-pane>
      <el-tab-pane label="评论" name="article"></el-tab-pane>
    </el-tabs>
    
  </div>
</template>
<script setup></script>
<style lang="scss">
.message-body {
  background-color: #fff;
  padding: 15px;
  .user-tabs {
  }
}
</style>
