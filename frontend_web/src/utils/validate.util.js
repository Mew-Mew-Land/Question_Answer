const Validate = {
  // 校验密码
  validatePass: () => (rule, value, callback) => {
    if (value === "") {
      callback(new Error("请再次输入密码"));
    } else if (value !== formData.password) {
      callback(new Error("密码不匹配！"));
    } else {
      callback();
    }
  },
  //   校验邮箱
  validateEmail: (rule, value, callback) => {
    const rules = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
    if (!rules.test(value)) {
      callback(new Error("邮箱不合规则"));
    } else {
      callback();
    }
  },
};

export default Validate;
