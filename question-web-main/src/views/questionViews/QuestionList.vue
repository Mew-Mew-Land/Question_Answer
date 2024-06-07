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
        <div v-for="(item, index) in questionData" :key="index">
          <div class="faq-list-item">
            <!-- 面板 -->
            <div class="num-tips-container">
              <div class="read-num">
                <img src="@/icon/9fcef5305e65ab4288ff910a7fbb1696.png" alt="Description of the image" />
              </div>
            </div>
            <div class="question-item">
              <div class="title">
                <RouterLink :to="`/faqDetail/${item.id}`" class="a-link">
                  {{ item.question }}
                </RouterLink>
              </div>
              <div class="userInfo">
                <div class="author">
                  作者：{{ item.username }}
                  <el-divider direction="vertical"></el-divider>
                  {{ item.updateTime }}
                </div>
                <span class="board">
                <RouterLink :to="`/faqDetail/${item.id}`" class="a-link">
                查看详情
                </RouterLink>
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="faq-side">
      <!-- 创作中心区域 -->
      <el-card class="box-card" shadow="never">
        <template #header>
          <div class="clearfix">
            <span class="card-header"><b><h2>创作中心</h2></b></span>
          </div>
        </template>
        <el-button-group class="button-group">
          <el-button type="info">
            <i class="iconfont icon-wancheng1"></i>
          </el-button>
          <el-button type="info">
            <i class="iconfont icon-bianjiwenzhang_huaban"></i>
          </el-button>
          <el-button type="info">
            <i class="iconfont icon-wodewenzhang"></i>
          </el-button>
          <el-button type="info">
            <i class="iconfont icon-gonggao"></i>
          </el-button>
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
            <span class="card-header"><b><h2>更多操作</h2></b></span>
          </div>
        </template>
        <div>
          <div class="action-item2">
            <el-button type="info">
              <i class="iconfont icon-shijian"></i>浏览记录
            </el-button>
          </div>
          <div class="action-item2">
            <el-button type="info">
              <i class="iconfont icon-good"></i> 我的点赞
            </el-button>
          </div>
          <div class="action-item2">
            <el-button type="info">
              <i class="iconfont icon-kong"></i> 我的收藏
            </el-button>
          </div>
          <div class="action-item2">
            <el-button type="info">
              <i class="iconfont icon-shanchu"></i> 我的举报
            </el-button>
          </div>
          <div class="action-item2">
            <el-button type="info">
              <i class="iconfont icon-gaojian-zuozhe"></i> 我的余额
            </el-button>
          </div>
          <div class="action-item2">
            <el-button type="info">
              <i class="iconfont icon-xiaoxi"></i> 系统设置
            </el-button>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { reactive, watch, ref, getCurrentInstance, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";

import { useMainStore } from "../../stores/index";

const store = useMainStore();
const route = useRoute();
const router = useRouter();
const { proxy } = getCurrentInstance();
const state = ref(false);
const userInfo = ref();

let loading = ref(false);
const questionData = ref({ list: [] });

const slides = ref([
  { image: 'https://p9.itc.cn/images01/20231114/9d5c8a0d5bea4de1b6cb88c4e4d79af9.jpeg' },
  { image: 'https://uploadfile.bizhizu.cn/up/91/e6/19/91e619947b3205fff47fb6161308b4b4.jpg.source.jpg' },
  { image: 'https://img1.baidu.com/it/u=3263265934,2210313069&fm=253&app=120&size=w931&n=0&f=JPEG&fmt=auto?sec=1716138000&t=0c88bc017b2b0bf9230049beec4211a0' },
]);

const getQuestionList = async (classificationId) => {
  try {
    //返回内容{
  //   "code": 200,
  //       "msg": "success",
  //       "data": [
  //     {
  //       "id": 5,
  //       "question": "aadsaw",
  //       "classificationId": 1,
  //       "answerNum": 0,
  //       "isSolved": 0,
  //       "updateTime": null,
  //       "userId": 0,
  //       "username": null,
  //       "viewNum": 0
  //     },
  //   ]
  // }
    let result = await proxy.Request({
      url: "/question/faqList",
      params: {
        classificationId: classificationId,
      },
      errorCallback: () => {
        proxy.Message.error("请求失败");
      },
    });

    // 检查 result 是否为空或未定义
    if (!result || !result.data) {
      console.error('API 返回了 null 或者不符合预期的结构:', result);
      // 可以设置一个默认值或者其他处理方式
      questionData.value = [];
      state.value = false; // 也可以根据需要设置其他状态
    } else {
      questionData.value = result.data;
      state.value = true;
    }
  } catch (error) {
    console.error('获取问题列表时出错:', error);
    proxy.Message.error("请求失败");
    // 错误处理逻辑，例如设置默认值
    questionData.value = [];
    state.value = false; // 也可以根据需要设置其他状态
  }
};


const activeTab = ref("null");
const changeTab = (tab) => {
  activeTab.value = tab;
};


onMounted(() => {
  // 初始获取问题列表
  getQuestionList(0);
});
// 监视路由参数的变化
watch(
    () => route.params,
    (newVal, oldVal) => {
      const classificationId = newVal.classificationId ?? 0;  // 如果 boardId 为空则传入 0
      getQuestionList(classificationId);
      userInfo.value = store.loginUserInfo;
    },
    { immediate: true, deep: true }
);

watch(
    () => store.loginUserInfo,
    (newVal, oldVal) => {
      userInfo.value = newVal || {};
    },
    { immediate: true, deep: true }
);


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

.faq-list-body {
  display: flex;
  min-height: 100vh;
}

.faq-main {
  background-color: #ffffff;
  flex: 1;
  opacity: 0.9;

  .el-tabs {
    height: 40px;
  }

  .el-tabs__header {
    margin-top: 15px;
    margin-left: 0;
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

  .el-card {
    opacity: 0.9;
  }

  .el-card__header {
    width: 300px;
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

.el-card {
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
  width: 200px;
  height: 50px;
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
      align-items: center;
      justify-content: center;
      padding: 8px;
      color: #fff;
      border: 2px solid #000;
      border-radius: 10px;
      box-shadow: 2px 2px 5px #888;
      margin-right: 10px;
      width: 100px;
      height: 100px;
    }

    .read-num img {
      width: 100%;
      height: 100%;
      object-fit: cover;
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
