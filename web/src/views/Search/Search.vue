<template>
  <div class="container-body search-main">
    <div class="search-header">搜索结果</div>

    <div class="search-list">
      <el-skeleton v-if="!loadingState" :rows="5" animated />
      <div class="faq-list" v-else>
      <DataList :loading="loading" :dataSource="searchData">
          <template #default="{ data }">
            <QuestionListItem :data="data"></QuestionListItem>
          </template>
        </DataList>
        <div class="pagination">
          <el-pagination
              background
              layout="prev, pager, next"
              :total="searchData.pagination.total"
              v-model:current-page="searchData.pagination.page"
              @current-change="handelPageNoChange"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { question } from "../../utils/api.utils";
import QuestionListItem from "../questionViews/QuestionListItem.vue";
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
    url: question.search,
    params: {
      page: currentPage.value,
      pageSize: 10,
      keywords: route.params.keyword,
    },
  });
  if (!result) return;
  searchData.value = result.data;
  loadingState.value = true;
};

const currentPage = ref(1);
const handelPageNoChange = (pageNo) => {
  currentPage.value = pageNo;
  searchQuestion();
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
