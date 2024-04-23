package org.exmple.mysqlbatis.filter;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.exmple.mysqlbatis.entity.Result;
import org.exmple.mysqlbatis.utils.TokenUtil;
import org.springframework.util.StringUtils;

import java.io.IOException;
@Slf4j
@WebFilter(urlPatterns ="/*")//拦截所有
public class DemoFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        System.out.println("创建好了");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("成功拦截了请求");
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        response.setCharacterEncoding("UTF-8");
        String url=request.getRequestURL().toString();
        log.info("请求的URL: "+url+"\n");
        if(url.contains("login")){
            log.info("登录操作，放行...");
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        String token = request.getHeader("token");
        if(!StringUtils.hasLength(token)){
            log.info("请求头token为空, 返回未登录信息");
            Result re = Result.error("未登录");
            String no_login=JSONObject.toJSONString(re);
            response.getWriter().write(no_login);
            return;
        }
        try{
            TokenUtil.parseClaim(token);
        }catch(Exception e){
            e.printStackTrace();
            log.info("解析令牌失败");
            Result re = Result.error("未登录");
            String no_login=JSONObject.toJSONString(re);
            response.getWriter().write(no_login);
            return;
        }
        //放行
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        System.out.println("成功销毁Filter");
    }
}
