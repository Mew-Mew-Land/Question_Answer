<template>
  <div>
    <div>
      <button @click="goToHome">返回主页面</button>
    </div>

  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const goToHome = () => {
  router.push('/'); // 使用路由的push方法导航回主页
};
</script>

<style scoped>
button {
  padding: 10px 15px;
  background-color: #3498db;
  border: none;
  color: white;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
}
button:hover {
  background-color: #2980b9;
}
</style>
