// 引入mockjs
//mockjs是一个用于生成随机数据、拦截Ajax请求的JavaScript库
const Mock = require('mockjs')
// 获取 mock.Random 对象
// 参考：https://github.com/nuysoft/Mock/wiki/Mock.Random
//Random是mockjs提供的一个工具对象，包含了大量的方法用于生成各种随机数据，如随机姓名、日期、邮箱等。
const Random = Mock.Random
// 定义了一个名为Result的变量，用于存储模拟接口的返回结果。
let Result = {
    code: 200,
    msg: '操作成功',
    data: null
}
/**
 * Mock.mock( url, post/get , function(options))；
 * url 表示需要拦截的 URL，
 * post/get 需要拦截的 Ajax 请求类型
 * 用于生成响应数据的函数
 */
//模拟了一个GET请求到/captcha路径的接口
Mock.mock('/captcha', 'get', () => {
    Result.data = {
        token: Random.string(32), // 获取一个32位的随机字符串,
        captchaImg: Random.dataImage( "120x40", "11111" ) //生成验证码为11111的base64图片编码
    }
    return Result
})


Mock.mock(RegExp('/login.*'), 'post', (config) => {
    // 模拟登录逻辑
    console.log("mock----------------login");
    // 直接返回成功的状态码和模拟的JWT
    return {
        code: 200,
        msg: '登录成功',
        data: {
            jwt: '模拟的JWT令牌'
        }
    };
});

