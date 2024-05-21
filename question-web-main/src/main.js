import { createApp } from "vue";
import { createPinia } from "pinia";
import App from "./App.vue";
import router from "./router";
import "./assets/main.scss";
import "./assets/iconfont/iconfont.scss";
import "element-plus/dist/index.css";

// 导入全局组件
import Avatar from "./components/Avatar.vue";
import EditorMarkdown from "./components/EditorMarkdown.vue";
import EditorHtml from "./components/EditorHtml.vue";
import DataList from "./components/DataList.vue";


// 全局方法
import message from "./utils/Message.utils.js";
import request from "./utils/Request.util.js";
import Confirm from "./utils/Confirm.util";
import { transformIsoDate } from "./utils/time.utils";
import Validate from "./utils/validate.util";

const pinia = createPinia();
const app = createApp(App);

app.config.globalProperties.Message = message;
app.config.globalProperties.Request = request;
app.config.globalProperties.Confirm = Confirm;
app.config.globalProperties.TransformIsoDate = transformIsoDate;
app.config.globalProperties.Validate = Validate;
// 调用全局组件
app.component("Avatar", Avatar);
app.component("EditorMarkdown", EditorMarkdown);
app.component("EditorHtml", EditorHtml);
app.component("DataList", DataList);

app.use(pinia);
app.use(router);
app.mount("#app");
