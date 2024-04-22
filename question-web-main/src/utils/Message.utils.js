import { ElMessage } from "element-plus";
const showMessage = (msg, callBack, type) => {
  ElMessage({
    type: type,
    message: msg,
    duration: 2000,
    onClose: () => {
      if (callBack) {
        callBack();
      }
    },
  });
};

const message = {
  error: (msg, callBack) => {
    showMessage(msg, callBack, "error");
  },
  success: (msg, callBack) => {
    showMessage(msg, callBack, "success");
  },
  warning: (msg, callBack) => {
    showMessage(msg, callBack, "warning");
  },
};

export default message;
