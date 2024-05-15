<template>

  <div class="skeleton" v-if="loading">

    <el-skeleton :row="2" animated></el-skeleton>
  </div>
  <!-- 数据加载完毕后显示数据列表 -->
  <div v-for="(item, index) in dataSource.list" v-else>
    <slot :data="item" :index="index"></slot>
  </div>
</template>


<script setup>
import { onMounted } from "vue";

const props = defineProps({
  dataSource: {
    type: Object,
  },
  loading: {
    type: Boolean,
  },
  noDataMsg: {
    type: String,
    default: "暂无数据",
  },
});

</script>
<style lang="scss">
.skeleton {
  padding: 15px;
}
</style>
