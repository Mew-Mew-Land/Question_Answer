<template>
  <!-- 骨架屏加载动画 -->
  <div class="skeleton" v-if="loading">
    <el-skeleton :row="2" animated></el-skeleton>
  </div>
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

// console.log(props.dataSource);
</script>
<style lang="scss">
.skeleton {
  padding: 15px;
}
</style>
