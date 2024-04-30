// 引入Mock.js
import Mock from 'mockjs';

// 模拟登录API
Mock.mock('/api/login', 'post', (options) => {
    // 解析请求体中的数据
    const { username, password } = JSON.parse(options.body);

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
