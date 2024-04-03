import axios from "axios";
import { ElMessage } from 'element-plus' // 从 Element Plus 中导入 ElMessage
import router from "./router";

//axios.defaults.baseURL = "http://localhost:8081"

const request = axios.create({
    //请求的超时时间为5000毫秒
    timeout: 5000,
    //默认请求头。这里指定了Content-Type告诉服务器，客户端发送的数据格式是JSON，并且字符集为UTF-8。
    headers: {
        'Content-Type': 'application/json; charset=utf-8'
    }
})

// 请求拦截器
//方法用于注册请求拦截器，它接收一个函数作为参数。这个函数会在请求发出之前被调用，并且接收请求的配置对象config作为参数
request.interceptors.request.use(config => {
    //将本地存储（localStorage）中的token值检索出来，并设置到请求的Authorization头部中
    config.headers['Authorization'] = localStorage.getItem("token"); // 请求头带上token
    return config;
})

// 响应拦截器
request.interceptors.response.use(response => {
        let res = response.data;
        console.log("response");
        console.log(res);
        if (res.code === 200) {
            return response;
        } else {
            // 使用Element Plus的消息提示组件
            //duration属性用来设置消息显示的持续时间
            //RU果res.msg存在（即后端返回了错误消息），则显示res.msg
            ElMessage.error(res.msg ? res.msg : '系统异常！', { duration: 3000 });
            //Promise.reject方法返回一个被拒绝的Promise对象
            return Promise.reject(response.data.msg);
        }
    },
    error => {
        console.log(error);
        if (error.response.data) {
            error.message = error.response.data.msg;
        }
        if (error.response.status === 401) {
            // 未授权，重定向到登录页面
            router.push("/login");
        }
        // 错误提示
        ElMessage.error(error.message, { duration: 3000 });
        return Promise.reject(error);
    }
)

export default request;
