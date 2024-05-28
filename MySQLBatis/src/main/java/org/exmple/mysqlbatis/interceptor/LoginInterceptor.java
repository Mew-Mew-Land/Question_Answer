package org.exmple.mysqlbatis.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.exmple.mysqlbatis.entity.Result;
import org.exmple.mysqlbatis.entity.User;
import org.exmple.mysqlbatis.utils.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override//目标资源方法运行前运行, true则放行,false则不放行
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding("UTF-8");
        String token = request.getHeader("token");
        if(!StringUtils.hasLength(token)){
            log.info("请求头token为空, 返回未登录信息");
            Result re = Result.error("未登录");
            String no_login= JSONObject.toJSONString(re);
            response.getWriter().write(no_login);
            return false;
        }
        try{
            TokenUtil.parseClaim(token);
        }catch(Exception e){
            e.printStackTrace();
            log.info("解析令牌失败");
            Result re = Result.error("登录已经失效，请重新登录");
            String no_login=JSONObject.toJSONString(re);
            response.getWriter().write(no_login);
            return false;
        }
        return true;
    }

    @Override//目标资源方法运行之后运行
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override//视图渲染完成后运行,最后才会运行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
