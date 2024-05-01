import axios from "axios";
import { ElLoading } from "element-plus";
import Message from "./Message.utils";

// 定义常量表示 JSON 内容类型，通常用于 HTTP 请求的头部（Header）中指定内容格式。
const contentTypeJson = "application/json";

// 开发环境的基础 URL，用于本地或测试环境的数据交互。
const devUrl = "http://localhost:3000";

// 创建 axios 实例，并配置基础 URL 和请求超时时间。
const instance = axios.create({
  baseURL: devUrl, // 将基础 URL 设置为开发环境的 URL。
  timeout: 10 * 1000, // 设置超时时间为 10,000 毫秒（10秒）。
});
// 初始化 loading 变量，用于存放加载动画的实例。
let loading = null;

//添加请求拦截器
// instance.interceptors.request.use(
//     (config) => {
//       // 在发送请求之前检查配置中是否需要显示加载动画。
//       if (config.showLoading) {
//         // 'lock' 锁定屏幕，用户不能进行其他操作。
//         // 'text' 设置加载动画中显示的文字。
//         // 'background' 设置加载动画的背景颜色。
//         loading = ElLoading.service({
//           lock: true,
//           text: "加载中", // 显示的文本
//           background: "rgba(0,0,0,0.7)" // 半透明黑色背景
//         });
//       }
//       return config; // 返回配置对象，继续发送请求。
//     },
//     (error) => {
//       // 请求错误处理
//       if (error.config.showLoading && loading) {
//         // 如果有加载动画实例，关闭它。
//         loading.close();
//       }
//       // 使用 Element UI 的消息提示组件显示错误信息。
//       Message.error("请求发送失败");
//       // 返回被拒绝的 promise，中断请求流程。
//       return Promise.reject("请求发送失败");
//     }
// );

// 请求后过滤器
// 添加响应拦截器
// instance.interceptors.response.use(
//     (response) => {
//       // 从响应的配置中解构出需要的配置项
//       // const { showLoading, errorCallback, showError } = response.config;
//       //
//       // // 如果配置了显示加载动画，并且加载动画实例存在，则关闭加载动画
//       // if (showLoading && loading) {
//       //   loading.close();
//       // }
//
//       // 获取响应体数据
//       const responseData = response.data;
//
//       // 检查响应数据中的状态码，200 通常表示请求成功
//       if (responseData.code == 200) {
//         // 如果状态码为200，直接返回响应数据
//         return responseData;
//       } else {
//         // 如果状态码不是200，检查是否提供了错误回调函数
//         if (errorCallback) {
//           // 如果有错误回调函数，则调用它处理错误数据
//           errorCallback(responseData);
//         }
//         // 返回一个被拒绝的Promise，包含错误信息和是否显示错误的标志
//         return Promise.reject({ showError: showError, msg: responseData.info });
//       }
//     },
//     (error) => {
//       // 错误处理逻辑
//       console.log(error.response.data.message); // 控制台打印错误信息，用于调试
//
//       // 如果配置了显示加载动画，并且加载动画实例存在，则关闭加载动画
//       // if (error.config.showLoading && loading) {
//       //   loading.close();
//       // }
//
//       // 返回一个被拒绝的Promise，包含错误信息和一个标志位指示总是显示错误
//       return Promise.reject({
//         showError: true,
//         msg: error.response.data.message,
//       });
//     }
// );


// 定义异步函数 request
const request = async (config) => {
  console.log(config);
  // 通过解构赋值从 config 对象中提取所需的属性在 JavaScript 中使用解构赋值时，如果从对象中解构不存在的属性，这些属性会被赋予 undefined
  const {
    url, // 请求的 URL
    params, // 请求的参数
  } = config;

  // 设置内容类型，默认为 JSON
  let contentType = contentTypeJson;

  // 创建 FormData 对象，用于构建要发送的表单数据
  let formData = new FormData();
  // 为params每个参数添加到 formData 中，如果参数值为 undefined，则替换为空字符串
  for (let key in params) {
    formData.append(key, params[key] == undefined ? "" : params[key]);
  }

console.log(formData);

  // 从浏览器的 localStorage 中获取 token，并构造授权头部
  //Authorization：包括从 localStorage 获取的 JWT（JSON Web Token）来验证请求的身份。
  // Content-Type：设置为之前定义的 contentType。
  // X-Requested-With：这个头部用于标识 AJAX 请求，有时用于服务器端判断请求是不是由 AJAX 发起。
  //let token = "Bearer " + (localStorage.getItem("token") || "");
  let headers = {
    "Content-Type": contentType, // 设置内容类型头部
    //"X-Requested-With": "XMLHttpRequest", // 表示这是一个 AJAX 请求
    //Authorization: token, // 设置授权头部
  };

  try {
    // 使用 axios 实例发送 POST 请求
    return await instance.post(url, formData ,{headers});
  } catch (error) {
    // 错误处理：如果配置了显示错误消息，则显示错误
    if (error.showError) {
      Message.error(error.msg);
    }
    // 出现错误时返回 null
    return null;
  }
};


export default request;
