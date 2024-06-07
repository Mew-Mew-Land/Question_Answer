<template>
  <div class="container-body search-main">
    <div class="search-header">搜索结果</div>

    <div class="search-list">

      <div class="faq-list">
        <div v-for="(item, index) in searchData" :key="index">
          <div class="faq-list-item">
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
                  作者名
                  <el-divider direction="vertical"></el-divider>
                  {{ item.question }}
                </div>
                <span class="board">
                  <RouterLink class="a-link" :to="`/faq/${item.classificationId}`">
                    查看详情
                  </RouterLink>
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>


import { watch, ref, getCurrentInstance, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
const route = useRoute();
const router = useRouter();
const { proxy } = getCurrentInstance();

const loadingState = ref(false);

const searchData = ref({});

// 搜索方法
const searchQuestion = async () => {
  

  let result = await proxy.Request({
    url: "/search",
    params: {
      keywords: route.params.keyword,
    },
  });
  if (!result) return;
  searchData.value = result.data;
};

watch(
  () => route.params.keyword,
  (newVal, oldVal) => {
    if (newVal) {
      searchQuestion();
    }
  },
  { immediate: true, deep: true }
);
onMounted(() => {
  searchQuestion();
});
</script>
<style lang="scss">
.search-main {
  background-color: #fff;
  .search-header {
    padding: 1vw;
    border-bottom: #efefef 1px solid;
  }
}
</style>
