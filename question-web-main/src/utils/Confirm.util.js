import { ElMessageBox } from "element-plus";
// Confirm 函数: Confirm 是一个函数，接受两个参数：message 和 okfun。message 是要显示在确认框中的消息文本，而 okfun 是当用户点击“确定”按钮时将被调用的函数。
// ElMessageBox.confirm: 在函数内部，使用 ElMessageBox.confirm 方法来显示确认框。这个方法接受三个参数：
// 第一个参数是 message，即传入的消息文本。
// 第二个参数是对话框的标题，这里设置为“提示”。
// 第三个参数是一个对象，包含对话框的配置选项：
// confirmButtonText: 确定按钮的文本，这里设为“确定”。
// cancelButtonText: 取消按钮的文本，这里设为“取消”。
// type: 对话框的类型，这里设置为“info”，表示信息提示框。
//学习什么是Promise
// Promise 处理:
//     .then(async () => { okfun(); }): 当用户点击“确定”按钮时，会执行 okfun 函数。这里使用了 async 关键字，表明 okfun 可能是一个异步函数。
// .catch(() => {}): 如果用户点击“取消”按钮，或者关闭对话框，catch 会捕捉到拒绝的 Promise，但这里并没有执行任何操作。
//导出Confirm函数: export default Confirm; 这一行将 Confirm 函数作为默认导出，使得其他文件可以通过 import Confirm from '路径' 的方式引入并使用这个函数。
const Confirm = (message, okfun) => {
  //ElMessageBox.confirm 是来自 element-plus UI 库的一个方法，用于创建一个确认对话框（confirm dialog）。
  //ElMessageBox.confirm 方法返回的是一个 Promise 对象。Promise 是JavaScript中用于异步操作的对象，它代表了一个将来会完成的操作，并承诺在未来某个时刻提供一个结果。这个结果可能是操作成功的值（resolved）或操作失败时的错误（rejected）。
  ElMessageBox.confirm(message, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "info",
  })
    .then(async () => {
      okfun();
    })
    .catch(() => {});
};

export default Confirm;
