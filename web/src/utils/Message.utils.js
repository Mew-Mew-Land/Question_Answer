//ElMessage 是来自 element-plus UI 库的一个组件，它用于在Web页面上显示一条信息通知。
import { ElMessage } from "element-plus";
const showMessage = (msg, callBack, type) => {
  //在JavaScript中，函数可以接受任何类型的参数，包括数字、字符串、对象等。在 ElMessage 的调用中、
  //这里 ElMessage 接受一个对象作为参数。这个对象包含了几个属性，这些属性定义了通知的行为和显示内容：
  ElMessage({
    type: type, // 消息类型，如 'error', 'success', 'warning'
    message: msg, // 显示的消息文本
    duration: 2000, // 消息框显示的持续时间（毫秒）
    onClose: () => { // 消息框关闭时的回调函数
      if (callBack) {
        callBack(); // 如果提供了回调函数，则在消息框关闭后执行
      }
    },
  });
};
//在JavaScript中，{} 用来定义对象。这种方式称为对象字面量语法，它允许直接在花括号中定义对象的属性和方法。
const message = {
  //这里定义了一个名为 message 的对象，它包含三个方法：error、success 和 warning。每个方法都是一个箭头函数，接受 msg 和 callBack 作为参数，并调用 showMessage 函数。
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
