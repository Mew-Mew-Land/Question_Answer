<template>
  <!-- 骨架屏加载动画 -->
<!--  v-if为真才显示-->
  <div class="skeleton" v-if="loading">
    <!--
      使用Element UI的el-skeleton组件创建动画效果的骨架屏。
      :row="2" 表示骨架屏包含两行。
      animated属性使骨架屏有动画效果。
    -->
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

// console.log(props.dataSource);
</script>
<style lang="scss">
.skeleton {
  padding: 15px;
}
</style>
