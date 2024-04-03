import { createRouter, createWebHistory } from 'vue-router';
import Login from "../views/Login.vue";

// 定义路由
const routes = [
  {
    path: '/',
    name: 'Home',

    children: [
      {
        path: '/index',
        name: 'Index',
        meta: {
          title: "首页"
        },
        component: () => import('@/views/Index.vue')
      }
    ]
  },


  {
    path: '/login',
    name: 'Login',
    component:  () => import('../views/Login.vue')
  }
];

// 使用 createRouter 创建路由实例
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL), // 使用 createWebHistory
  routes
});

export default router;
