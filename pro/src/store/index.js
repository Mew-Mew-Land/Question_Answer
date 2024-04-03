// store/index.js
import { createStore } from 'vuex'

// 创建一个新的 store 实例
const store = createStore({
  state() {
    return {
      token: ''
    }
  },
  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token;
      localStorage.setItem("token", token); // 可能想在action中做这个以处理异步操作
    }
  },
  actions: {
    // 这里可以定义一些action，尤其是异步操作
  },
  modules: {
    // 如果你有模块化的需求，可以在这里配置模块
  }
})

export default store;
