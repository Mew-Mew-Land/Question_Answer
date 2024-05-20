<template>


  <!-- 数据加载完毕后显示数据列表 -->
  <div v-for="(item, index) in dataSource.list" >
    <slot :data="item" :index="index"></slot>
  </div>
</template>


<script setup>
import { onMounted } from "vue";

const props = defineProps({
  dataSource: {
    type: Object,
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
