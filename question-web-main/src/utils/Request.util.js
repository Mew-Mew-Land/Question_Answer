import axios from "axios";
import { ElLoading } from "element-plus";
import Message from "./Message.utils";

const contentTypeJson = "application/json";
// 生产环境
const productionUrl = "http://1.12.237.245:8002";
// 开发环境
const devUrl = "http://localhost:3000";
const instance = axios.create({
  baseURL: devUrl,
  timeout: 10 * 1000,
});
// 请求前过滤器
let loading = null;
instance.interceptors.request.use(
  (config) => {
    if (config.showLoading) {
      loading = ElLoading.service({
        lock: true,
        text: "加载中",
        background: "rgba(0,0,0,0.7)",
      });
    }
    return config;
  },
  (error) => {
    if (error.config.showLoading && loading) {
      loading.close();
    }
    Message.error("请求发送失败");
    return Promise.reject("请求发送失败");
  }
);

// 请求后过滤器
instance.interceptors.response.use(
  (response) => {
    const { showLoading, errorCallback, showError } = response.config;
    if (showLoading && loading) {
      loading.close();
    }

    const responseData = response.data;
    if (responseData.code == 200) {
      return responseData;
    } else {
      if (errorCallback) {
        errorCallback(responseData);
      }
      return Promise.reject({ showError: showError, msg: responseData.info });
    }
  },
  (error) => {
    console.log(error.response.data.message);
    if (error.config.showLoading && loading) {
      loading.close();
    }
    return Promise.reject({
      showError: true,
      msg: error.response.data.message,
    });
  }
);

const request = async (config) => {
  const {
    url,
    params,
    showLoading = true,
    errorCallback,
    showError = true,
  } = config;
  let contentType = contentTypeJson;
  let formData = new FormData();
  for (let key in params) {
    formData.append(key, params[key] == undefined ? "" : params[key]);
  }
  // 从浏览器缓存中获取token
  let token = "Bearer " + (localStorage.getItem("token") || "");
  let headers = {
    "Content-Type": contentType,
    "X-Requested-With": "XMLHttpRequest",
    Authorization: token,
  };
  try {
    return await instance.post(url, formData, {
      headers: headers,
      showLoading: showLoading,
      errorCallback: errorCallback,
      showError: showError,
    });
  } catch (error) {
    if (error.showError) {
      Message.error(error.msg);
    }
    return null;
  }
};

export default request;
