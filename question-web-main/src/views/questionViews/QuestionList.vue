<template>
  <div class="container-body faq-list-body">

    <!-- 问题列表与右侧公告栏 -->
    <div class="faq-main">
      <!-- 轮播图 -->
      <el-carousel :interval="5000" arrow="always" class="faq-carousel">
        <el-carousel-item v-for="(slide, index) in slides" :key="index">
          <img :src="slide.image" class="carousel-image" />
        </el-carousel-item>
      </el-carousel>
      <el-tabs v-model="activeTab" class="faq-header" @tab-change="changeTab">
        <el-tab-pane label="按发布时间" name=""></el-tab-pane>
        <el-tab-pane label="按浏览次数" name="0"></el-tab-pane>
        <el-tab-pane label="按回答数量" name="1"></el-tab-pane>
      </el-tabs>




      <el-skeleton v-if="!state" :rows="5" animated />



      <div class="faq-list" v-else>
        <DataList :loading="loading" :dataSource="questionData">
          <template #default="{ data }">
            <QuestionListItem :data="data"></QuestionListItem>
          </template>
        </DataList>

<!--        <div class="pagination">-->
<!--          <el-pagination-->
<!--            background-->
<!--            layout="prev, pager, next"-->
<!--            v-if="questionData.pagination.pages > 1"-->
<!--            :total="questionData.pagination.total"-->
<!--            v-model:current-page="questionData.pagination.page"-->
<!--            @current-change="handelPageNoChange"-->
<!--          />-->
<!--        </div>-->
      </div>

      <!-- <div class="faq-list"></div> -->
    </div>
<!--    <div class="faq-side">-->
<!--      <div class="side-signin">-->
<!--        <div class="signin">-->
<!--          <span class="userInfo">👋Hi！ {{ userInfo.nickName }}</span>-->
<!--          &lt;!&ndash; <span class="time">{{ currentTime() + "好" }}</span> &ndash;&gt;-->
<!--        </div>-->
<!--        <div class="quote">这是一个技术问答平台，发表你的问题吧！👋</div>-->
<!--      </div>-->
<!--      <div class="side-acc">-->
<!--        <el-card class="box-card" style="box-shadow: none">-->
<!--          <template #header>-->
<!--            <div class="card-header">-->
<!--              <h3>公告</h3>-->
<!--            </div>-->
<!--          </template>-->
<!--          <span>暂无公告</span>-->
<!--        </el-card>-->
<!--      </div>-->
<!--    </div>-->


    <div class="faq-side">
      <!-- 创作中心区域 -->
      <el-card class="box-card" shadow="never">
        <template #header>
          <div class="clearfix">
            <span class="card-header" ><B><h2>创作中心</h2></B></span>
          </div>
        </template>
        <el-button-group class="button-group">
          <el-button type="info" >
            <i class="iconfont icon-wancheng1"></i></el-button>
          <el-button type="info" >
            <i class="iconfont icon-bianjiwenzhang_huaban"></i></el-button>
          <el-button type="info" >
            <i class="iconfont icon-wodewenzhang"></i></el-button>
          <el-button type="info" >
            <i class="iconfont icon-gonggao"></i></el-button>
        </el-button-group>

        <div class="labels">
          <span>编辑</span>
          <span>写文章</span>
          <span>写想法</span>
          <span>通知</span>
        </div>

      </el-card>

      <!-- 更多操作区域 -->
      <el-card class="box-card2" style="margin-top: 10px;" shadow="never">
        <template #header>
          <div class="clearfixel-card is-never-shadow box-card2">
            <span class="card-header"><B><h2>更多操作</h2></B></span>
          </div>
        </template>
        <div >
          <div class="action-item2">
            <el-button type="info" >
              <i class="iconfont icon-shijian"></i>浏览记录</el-button>
          </div>
          <div class="action-item2">
            <el-button type="info" >
              <i class="iconfont icon-good"></i> 我的点赞</el-button>
          </div>
          <div class="action-item2">
            <el-button type="info" >
              <i class="iconfont icon-kong"></i> 我的收藏</el-button>
          </div>
          <div class="action-item2">
            <el-button type="info" >
              <i class="iconfont icon-shanchu"></i> 我的举报</el-button>
          </div>
          <div class="action-item2">
            <el-button type="info" >
              <i class="iconfont icon-gaojian-zuozhe"></i> 我的余额</el-button>
          </div>
          <div class="action-item2">
            <el-button  type="info">
              <i class="iconfont icon-xiaoxi"></i> 系统设置</el-button>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>
<script setup>
import { reactive, watch, ref, getCurrentInstance, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import QuestionListItem from "./QuestionListItem.vue";
import { question } from "../../utils/api.utils";
import { useMainStore } from "../../stores/index";
const store = useMainStore();
const route = useRoute();
const router = useRouter();
const { proxy } = getCurrentInstance();
const state = ref(false);
const userInfo = ref();

let loading = ref(false);
const questionData = ref({});

const slides = ref([
  { image: 'https://p9.itc.cn/images01/20231114/9d5c8a0d5bea4de1b6cb88c4e4d79af9.jpeg' },
  { image: 'https://uploadfile.bizhizu.cn/up/91/e6/19/91e619947b3205fff47fb6161308b4b4.jpg.source.jpg' },
  { image: 'https://img1.baidu.com/it/u=3263265934,2210313069&fm=253&app=120&size=w931&n=0&f=JPEG&fmt=auto?sec=1716138000&t=0c88bc017b2b0bf9230049beec4211a0' },
]);
const getQuestionList = async (boardId) => {
  let result = await proxy.Request({
    url: question.faqList,
    params: {
      page: currentPage.value,
      pageSize: 10,
      boardId: boardId,
    },
    errorCallback: () => {
      proxy.Message.error("请求失败");
    },
  });
  if (!result) result;
  questionData.value = result.data;
  // 转换时间

  state.value = true;
  // console.log(questionData.list);
};
// getQuestionList();
const activeTab = ref("null");
const changeTab = (tab) => {
  activeTab.value = tab;
};

const currentPage = ref(1);
// 切换页
const handelPageNoChange = (pageNo) => {
  currentPage.value = pageNo;
  getQuestionList();
};

watch(
  () => route.params,
  (newVal, oldVal) => {

    if (newVal.boardId) {
      currentPage.value = 1;
      getQuestionList(newVal.boardId);
      userInfo.value = store.loginUserInfo;
    } else {
      getQuestionList();
    }
  },
  { immediate: true, deep: true }
);

watch(
  () => store.loginUserInfo,
  (newVal, oldVal) => {
    if (newVal != undefined && newVal != null) {
      userInfo.value = newVal;
    } else {
      userInfo.value = {};
    }
  },
  { immediate: true, deep: true }
);

onMounted(() => {});
</script>


<style lang="scss">

.box-card {
  width: 270px;
  margin-left: 50px;
}


.box-card2 {
  width: 150px;
  margin-left: 50px;
  margin-top: 30px;
}
.action-item2 {

  margin: 10px;
  display: flex;
  justify-content: center;
}

.action-item2 .el-button {
  border: none;
  color: white;
  font-weight: bold;

  padding: 10px 20px;
  border-radius: 5px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: background-color 0.3s, box-shadow 0.3s;
  width: 100px;
}

.el-button:hover {
  background-color: var(--mainColor);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}

.action-item .el-button {
  width: 50px;
  height: 45px;
}
.faq-list-body {
  display: flex;
  min-height: 100vh;
  .faq-main {
    background-color: #ffffff;
    flex: 1;
    opacity: 0.9;

    .el-tabs{
      height: 40px;
    }
    .el-tabs__header{
      margin-top: 15px;
      margin-left: 0 ;
    }
    .faq-header {
      min-height: 40px;
      display: flex;
      align-items: center;
      line-height: 40px;
      padding-left: 15px;


      .is-active {
          color: var(--mainColor);
      }
    }
    .faq-list {
      margin-top: -4px;
    }
  }
  .faq-side {
    width: 300px;
    margin-left: 10px;
    margin-top: 20px;
    text-align: center;

    .el-card{
      opacity: 0.9;
    }
    .el-card__header{
      width:300px;
    }
    .side-signin {
      display: flex;
      flex-direction: column;
      width: 100%;
      background-color: #ffffff;
      padding: 15px;
      margin-bottom: 1.2rem;
      .signin {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 10px;
        font-size: 1.2em;
        .userInfo {
        }
      }
      .quote {
        text-align: left;
      }
    }
    .side-acc {

      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }

      .box-card {
        width: 100%;
      }
    }
  }
  }

@media (max-width: 800px) {
  .faq-list-item {
    flex-direction: column;
  }
  .read-num {
    flex-direction: row;
  }
  .faq-side {
    display: none;
  }
  .num-tips-container {
    justify-content: flex-end;
  }
}
@media (min-width: 800px) {
  .read-num {
    flex-direction: column;
  }
  .num-tips-container {
    justify-content: space-around;
  }
}

.faq-carousel {
  margin-top: 0px;
  .el-carousel__item img {
    width: 100%;
    height: 300px;
    object-fit: cover;
    border-width: 3px;
    border-style: solid;
    border-image: linear-gradient(to right, #808080, #ffffff) 1;
    border-image-slice: 1;
  }
}

.button-group {
  display: flex;
  justify-content: space-around;
}

.button-group .el-button {
  margin: 5px;
  border-color: transparent;
  background-color: transparent;
  color: black;
}

.button-group .iconfont {
  font-size: 30px;
}

.el-card{
  width: 300px;
}

.labels span {
  display: inline-block;
  margin-right: 13px;
}


.labels span:last-child {
  margin-right: 0;
}

.action-item2 .el-button {
  background-color: transparent;
  color: black;
  border-color: black;
  font-size: 25px;
  padding: 15px 30px;
  width: 200px; /* 设置按钮的宽度 */
  height: 50px; /* 设置按钮的高度 */

}

.action-item2 .el-button:hover {
  background-color: rgba(0, 0, 0, 0.1);
}


.action-item2 .el-button.el-button--info {
  background-color: transparent;
  border-color: black;
  color: black;
}


.action-item2 .iconfont {
  font-size: 24px;
}
</style>
