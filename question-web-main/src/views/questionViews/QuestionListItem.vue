<template>
  <!-- 卡片 -->
  <div class="faq-list-item">

    <!-- 回答与阅读统计面板 -->
    <div class="num-tips-container">
<!--      <div-->
<!--        :class="[-->
<!--          'read-num',-->
<!--          data.isSolve ? 'issolve' : '',-->
<!--          data.answerCount ? 'answered' : '',-->
<!--        ]"-->
<!--      >-->
<!--        <span class="">{{ data.answerCount }}</span>-->
<!--        <span class="">{{ data.isSolve ? "解决" : "回答" }}</span>-->
<!--      </div>-->

        <div class="read-num" >
          <img src="@/icon/9fcef5305e65ab4288ff910a7fbb1696.png" alt="Description of the image" >
        </div>
      </div>








      <div class="question-item">
<!--data的属性:{ "questionId": 1, "title": "111", "createTime": "2024-05-01T12:00:00.320Z", "updateTime":
"2024-05-12T14:45:42.000Z", "viewCount": 25, "answerCount": 2,
"isSolve": false, "user": { "userId": 1 }, "board": { "boardId": 2, "boardName": "板块二" } }-->
      <div class="title">
        <RouterLink :to="`/faqDetail/${data.questionId}`" class="a-link">
          {{ data.title }}
        </RouterLink>
      </div>

      <div class="userInfo">
<!--        <div class="author">-->
<!--          <RouterLink class="a-link" :to="`/user/${data.user.userId}`">-->
<!--            {{ data.user.nickName }}-->
<!--          </RouterLink>-->

<!--          <el-divider direction="vertical"></el-divider>-->
<!--          {{ proxy.TransformIsoDate(data.createTime) }}-->
<!--        </div>-->
        <div class="author">

            作者名


          <el-divider direction="vertical"></el-divider>
          这是问题简介
        </div>
        <span class="board">
<!--          <RouterLink class="a-link" :to="`/faq/${data.board.boardId}`">-->
<!--            {{ data.board.boardName }}-->
<!--          </RouterLink>-->
           <RouterLink class="a-link" :to="`/faq/${data.board.boardId}`">
            查看详情
          </RouterLink>
        </span>

       
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

});
</script>
<style lang="scss">

.faq-list-item {
  display: flex;
  padding: 0.8em;
  background-image: url("@/icon/花纹.png");
  background-size: cover;
  margin-top: 5px;

  border-top: 1px var(--el-border-color) var(--el-border-style);




  .num-tips-container {
    display: flex;
    align-items: center;
    margin-right: 15px;

      .read-num {
        display: flex;
        align-items: center; /* 使内容垂直居中 */
        justify-content: center; /* 使内容水平居中 */
        padding: 8px;
        color: #fff;

        border: 2px solid #000;
        border-radius: 10px;
        box-shadow: 2px 2px 5px #888;
        margin-right: 10px;
        width: 100px; /* 设定固定宽度 */
        height: 100px; /* 设定固定高度 */
        overflow: hidden; /* 防止图片溢出 */
      }

      .read-num img {
        width: 100%; /* 图片宽度占满容器 */
        height: 100%; /* 图片高度占满容器 */
        object-fit: cover; /* 覆盖容器，保持图片的宽高完全适应容器，可能会裁剪图片 */
      }


    .answered {
      border: 1.8px var(--mainColor) solid;
      border-radius: 10%;
    }
    .issolve {
      color: #fff;
      background-color: var(--mainColor);
    }
  }

  .question-item {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    flex: 1;
    margin-bottom: 10px;


       margin-left: 10px;
    .answer{
      margin-left: 300px;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    }
    .button{
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
      background: linear-gradient(to right, #ff9966, #ff5e62);
      transform: scale(1.1);

    }
    .title {
    }
    .userInfo {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .author {
      }
      .board {
        background-color: #e9ecef;
        border-radius: 5px;
        padding: 5px;
        cursor: pointer;
      }
    }
  }
}
</style>
