import axios from "axios";
import Message from "./Message.utils";

// 设置请求的内容类型为 JSON
const contentTypeJson = "application/json";

// 生产环境的基础 URL
const productionUrl = "";

// 开发环境的基础 URL
const devUrl = "http://localhost:3000";

// 创建 axios 实例并配置基本选项
const instance = axios.create({
  baseURL: devUrl, // 使用开发环境的 URL
  timeout: 10 * 1000, // 请求超时时间设置为10秒
});

// 请求拦截器
instance.interceptors.request.use(
    (config) => {
      // 请求发送前可以进行的配置修改或添加功能
      return config;
    },
    (error) => {
      // 请求发生错误时的处理，如网络错误等
      Message.error("请求发送失败");
      return Promise.reject("请求发送失败");
    }
);

// 响应拦截器
instance.interceptors.response.use(
    (response) => {
      // 解构出响应中需要的配置项
      console.log(response);
      const { errorCallback, showError } = response.config;
      // 获取响应数据
      const responseData = response.data;

      // 检查响应状态码
      if (responseData.code == 200) {
        // 状态码为200时，返回响应数据
        return responseData;
      } else {
        // 状态码不为200时，调用错误回调函数处理错误
        if (errorCallback) {
          errorCallback(responseData);
        }
        return Promise.reject({ showError: showError, msg: responseData.info });
      }
    },
    (error) => {
      // 响应错误处理，如服务器返回的错误状态码
      console.log(error.response.data.message); // 打印错误消息以便调试
      return Promise.reject({
        showError: true, // 总是显示错误
        msg: error.response.data.message, // 错误消息
      });
    }
);

// 定义发送请求的异步函数
const request = async (config) => {
  const {
    url, // 请求的URL
    params, // 请求参数
    errorCallback, // 错误处理回调函数
    showError = true, // 是否显示错误，默认为 true
  } = config;

  // 设置请求的内容类型
  let contentType = contentTypeJson;
  // 创建 FormData 对象以发送表单数据
  let formData = new FormData();
  for (let key in params) {
    formData.append(key, params[key] == undefined ? "" : params[key]);
  }

  // 获取认证 token
  let token = "Bearer " + (localStorage.getItem("token") || "");
  // 设置请求头
  let headers = {
    "Content-Type": contentType,
    "X-Requested-With": "XMLHttpRequest", // 标识为 AJAX 请求
    Authorization: token, // 设置认证头
  };

  try {
    // 发送 POST 请求
    return await instance.post(url, formData, {
      headers: headers,
      errorCallback: errorCallback,
      showError: showError,
    });
  } catch (error) {
    // 错误处理
    if (error.showError) {
      Message.error(error.msg);
    }
    return null;
  }
};

export default request;
