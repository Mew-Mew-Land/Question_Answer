
import { createStore } from 'vuex'

// 使用 createStore 方法来创建一个新的 store 实例
export default createStore({
	// state 提供了一个响应式数据的状态对象
	state() {
		return {
			menuList: [],
			permList: [],
			hasRoutes: false,
			editableTabsValue: 'Index',
			editableTabs: [{
				title: '首页',
				name: 'Index',
			}]
		}
	},
	// mutations 提供了同步修改 state 的方法
	mutations: {
		// 设置菜单列表
		setMenuList(state, menus) {
			state.menuList = menus
		},
		// 设置权限列表
		setPermList(state, perms) {
			state.permList = perms
		},
		// 更改路由状态
		changeRouteStatus(state, hasRoutes) {
			state.hasRoutes = hasRoutes
		},
		// 添加一个新的标签页
		addTab(state, tab) {
			let index = state.editableTabs.findIndex(e => e.name === tab.name)
			if (index === -1) {
				state.editableTabs.push({
					title: tab.title,
					name: tab.name,
				});
			}
			state.editableTabsValue = tab.name;
		},
		// 重置状态
		resetState(state) {
			state.menuList = []
			state.permList = []
			state.hasRoutes = false
			state.editableTabsValue = 'Index'
			state.editableTabs = [{
				title: '首页',
				name: 'Index',
			}]
		}
	},
	// actions 提供了进行异步操作的方法
	actions: {

	},
})
