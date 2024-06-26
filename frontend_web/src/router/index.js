import { createRouter, createWebHistory } from "vue-router";
const router = createRouter({
  linkActiveClass: "a-active",
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [

    {
      path: "/",
      name: "Layout",
      component: () => import("@/views/Layout.vue"),
      children: [
        {
          path: "/login",
          name: "登录界面",
          component: () => import("@/views/LoginView.vue"),
        },
        {

          path: "/",
          name: "所有问题",
          component: () => import("@/views/questionViews/QuestionList.vue"),
        },
        {
          path: "/faq/:classificationId",
          name: "板块",
          component: () => import("@/views/questionViews/QuestionList.vue"),
        },

        {
          path: "/faqDetail/:questionId",
          name: "问题详情",
          component: () => import("@/views/questionViews/QuestionDetail.vue"),
        },
        {
          path: "/user/:userId",
          name: "用户中心",
          component: () => import("@/views/userCenter/UserInfo.vue"),
        },
        {
          path: "/search/:keyword",
          name: "搜索",
          component: () => import("@/views/Search/Search.vue"),
        },

        {
          path: "/newPost/",
          name: "发布问题",
          component: () => import("@/views/EditPost/EditPost.vue"),
        },
        {
          path: "/editPost/:questionId",
          name: "编辑文章或问题",
          component: () => import("@/views/EditPost/EditPost.vue"),
        },
      ],
    },

  ],
});

export default router;
