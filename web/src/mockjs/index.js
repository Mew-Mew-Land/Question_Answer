// 引入Mock.js
import Mock from 'mockjs';

console.log('在Mock.mock之前');
// 模拟登录API


Mock.mock('http://localhost:3000/api/login', 'post', (options) => {
    // 获取请求体中的 FormData 对象
    const formData = options.body;

    // 从 FormData 对象中获取字段值
    const username = formData.get('username');
    const password = formData.get('password');

    // 检查用户名和密码是否正确
    if (username === '111' && password === '111') {
        return {
            status: 200,
            message: '登录成功',
            data: {
                token: 'fake-token'
            }
        };
    } else {
        return {
            status: 401,
            message: '用户名或密码错误'
        };
    }
});

// 输出Mock以便可以在其他文件中使用
export default Mock;
