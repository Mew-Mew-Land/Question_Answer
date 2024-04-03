import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import request from './axios';

// 创建Vue应用实例
const app = createApp(App);

// 全局注册axios实例
app.config.globalProperties.$axios = request;

// 使用ElementPlus UI库
app.use(ElementPlus);

// 使用Vuex状态管理
app.use(store);

// 使用Vue Router
app.use(router);

// 挂载Vue应用到DOM
app.mount('#app');

// 引入mock数据，如果不需要可以注释掉
require("./mock");
