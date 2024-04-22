import { createApp } from 'vue';
import App from './App.vue'; // 假设你的根组件是App.vue
import store from './store'; // 假设你的Vuex store文件路径是./store

const app = createApp(App);

app.mixin({
	methods: {
		hasAuth(perm) {
			var authority = this.$store.state.menus.permList;
			return authority.indexOf(perm) > -1;
		}
	}
});

app.use(store);
app.mount('#app');
