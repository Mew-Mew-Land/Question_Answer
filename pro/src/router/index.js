import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
import Index from '../views/Index.vue';
import User from '../views/sys/User.vue';
import Role from '../views/sys/Role.vue';
import Menu from '../views/sys/Menu.vue';

import axios from "../axios";
import store from "../store";

import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import home from "@/views/Home.vue";

// 定义路由
const routes = [
  {
    path: '/',
    name: 'Home',
    // 当用户访问根路径 / 时，应该渲染 Home 组件
    component: Home,
    children: [
      {
        path: 'index',
        name: 'Index',
        meta: {
          title: "首页"
        },
        // 当用户访问 /index 时，应该渲染 Index 组件
        component: Index
      },
      {
        path: 'userCenter',
        name: 'UserCenter',
        meta: {
          title: "个人中心"
        },
        // 当用户访问 /userCenter 时，会动态加载 UserCenter.vue 组件
        component: () => import('@/views/UserCenter.vue')
      },
      {
      	path: 'sys/users',
      	name: 'SysUser',
      	component: User
      },
      {
      	path: 'sys/roles',
      	name: 'SysRole',
      	component: Role
      },
      {
      	path: 'sys/menus',
      	name: 'SysMenu',
        component:  () => import('../views/sys/Menu.vue')
      },
    ]
  },


  {
    path: '/login',
    name: 'Login',
    component:  () => import('../views/Login.vue')
  },
  {
    path: '/testslid',
    name: '1234',
    component:  () => import('../views/inc/SideMenu.vue')
  },
  {
    path: '/testhome',
    name: 'TESThome',
    component:Home
  },
  {
    path: '/testmenu',
    name: 'TESTmenu',
    component:Menu
  },
  {
    path: '/testuser',
    name: 'TESTuser',
    component:User
  },
  {
    path: '/testrole',
    name: 'TESTrole',
    component:Role
  }
];

// 创建了一个基于浏览器历史记录的路由器实例，并将其赋值给 router 常量。
// 我们使用了 createWebHistory 作为历史模式，并传递了应用程序的基本 URL。
// 路由配置存储在 routes 数组中
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL), // 使用 createWebHistory
  routes
});


// router.beforeEach: 这是一个 Vue Router 的导航守卫，用于在路由切换之前执行一些逻辑。在这里，我们检查用户的登录状态和路由权限。
// to: 这是一个路由对象，表示用户即将访问的路由。
// from: 这是一个路由对象，表示用户当前所在的路由。
// next: 这是一个函数，用于控制路由跳转。我们可以调用 next() 来继续路由跳转，或者传递一个新的路由路径来重定向用户。


router.beforeEach(async (to, from, next) => {
  let hasRoute = store.state.menus.hasRoutes;
  let token = localStorage.getItem("token");

  if (to.path === '/login') {
    next();
  } else if (!token) {
    next({ path: '/login' });
  } else if (token && !hasRoute) {
    try {
      const res = await axios.get("/sys/menu/nav", {
        headers: {
          Authorization: token
        }
      });

      // 拿到 menuList 和 用户权限
      store.commit("setMenuList", res.data.data.nav);
      store.commit("setPermList", res.data.data.authoritys);

      // 动态绑定路由
      res.data.data.nav.forEach(menu => {
        if (menu.children) {
          menu.children.forEach(e => {
            let route = menuToRoute(e); // 将菜单项转换为路由配置
            if (route) {
              router.addRoute('Home', route); // 注意：这里的 'Home' 应该是父级路由的名称
            }
          });
        }
      });

      // 更新路由加载状态
      store.commit("changeRouteStatus", true);
      next({ ...to, replace: true }); // 使用 replace: true 避免重复历史记录
    } catch (error) {
      console.error("Error loading menu:", error);
      next(false); // 出错时停止路由导航
    }
  } else {
    next(); // 确保在所有情况下调用 next()
  }
});



// 导航项转成路由配置对象
const menuToRoute = (menu) => {
  // 检查菜单项是否有对应的组件路径，如果没有则不转换为路由
  if (!menu.component) {
    return null;
  }

  // 创建路由配置对象
  let route = {
    name: menu.name, // 路由名称，通常用于 <router-link :to="{ name: 'name' }">
    path: menu.path, // 路由路径
    meta: {
      icon: menu.icon, // 路由元数据，可以存储任意信息，这里存储了图标和标题
      title: menu.title
    },
    // 动态导入组件，实现路由懒加载
    // 使用模板字符串构造组件的路径，假设所有的路由组件都存放在 '@/views/' 目录下
    component: () => import(`@/views/${menu.component}.vue`)
  };

  return route; // 返回构造好的路由配置对象
};


export default router;
