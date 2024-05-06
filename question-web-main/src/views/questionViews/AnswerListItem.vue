<template>
  <div :class="['answer-item', data.isAdopt ? 'isAdopt' : '']">
    <div class="answer-user-info">
      <Avatar></Avatar>
      <span class="answer-name">
        <RouterLink class="a-link" :to="`/user/${data.user.userId}`">
         
          {{ data.user.nickName }}
        </RouterLink>
      </span>
      <el-divider direction="vertical" />
      <span>{{ proxy.TransformIsoDate(data.createTime) }}</span>
    </div>
    <div v-if="data.isAdopt" class="answer-solvestate">
      <span>已被采纳</span>
    </div>
    <div class="answer-content" v-html="data.content"></div>

    <div class="answer-action">
      <!-- <el-button type="primary">点赞</el-button>
            <el-button type="primary">评论</el-button> -->
      <span class="answer-reply" @click="commentHandle(index)">回复</span>
      <el-button
        v-if="currentUserInfo.userId === data.user.userId"
        type="primary"
        @click="editAnswer(data)"
        >编辑</el-button
      >
      <el-button
        type="primary"
        v-if="
          currentUserInfo.userId === questionDetail.user.userId &&
          !questionDetail.isSolve
        "
        @click="adoptAnswer(data.answerId, index)"
        >采纳</el-button
      >
      <el-button
        v-if="currentUserInfo.userId === data.user.userId"
        type="danger"
        @click="delAnswer(data.answerId, index)"
        >删除</el-button
      >
    </div>
    <CommentBox :answerId="data.answerId" v-if="data.showReply"></CommentBox>

    <div class="reply-list">
      <div class="user-reply">
        前端搬运工：这需求不是求并集呢，而是去掉重复的数据
      </div>
      <div class="reply-action">
        <span>回复</span>
        <el-divider direction="vertical" />
        <span>4 月 14 日</span>
      </div>
    </div>
  </div>
</template>
<script setup>
import { getCurrentInstance } from "vue";
const { proxy } = getCurrentInstance();
const props = defineProps({
  data: {
    type: Object,
  },
  index: {
    type: Number,
  },
});



</script>
<style lang="scss">
.answer-item {
  // margin-bottom: 10px;
  background-color: #fff;
  padding: 15px;

  .answer-user-info {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
    .answer-name {
      margin-left: 10px;
    }
  }
  .answer-solvestate {
    span {
      border-radius: 10px;
      padding: 5px;
      background-color: var(--mainColor);
      font-size: 0.5em;
      color: #fff;
    }
  }
  .answer-content {
    margin-top: 20px;
  }
  .answer-action {
    padding: 1vw 0;
    .answer-reply {
      margin-right: 10px;
      cursor: pointer;
    }
  }
  .reply-list {
    background-color: #e9ecef;
    padding: 10px;
    .user-reply {
      margin-bottom: 5px;
    }
    .reply-action {
    }
  }
}
.isAdopt {
  border: 2px solid var(--mainColor);
}
</style>
