
import { createStore } from 'vuex';
// 导入菜单模块
import menus from "./modules/menus";

// 创建一个新的 store 实例
const store = createStore({
  // state 提供了存储全局状态的响应式数据对象
  state() {
    return {
      // 初始化一个空的 token 字符串
      token: ''
    };
  },
  // mutations 提供了用来同步修改 state 的方法
  mutations: {
    // 设置 token 的 mutation
    // 接受当前的 state 和要设置的 token 值作为参数
    SET_TOKEN: (state, token) => {
      // 更新 state 中的 token
      state.token = token;
      // 将新的 token 值存储到 localStorage 中，以便跨会话保留 token
      localStorage.setItem("token", token);
    }
  },

  actions: {
  },

  modules: {
    menus
  }
});

export default store;
