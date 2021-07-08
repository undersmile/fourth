package club.guoshizhan.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 这是一个拦截器，用于拦截路径中带有 admin 的 URL，即如果没有登录，那么有些需要登录之后才能操作的页面是无法访问的
 * 这个拦截器就像一张网一样，能够拦截一些指定的 URL。注意：一定要继承：HandlerInterceptorAdapter
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    // preHandle 是一个预处理操作，在访问有关 admin 相关的 URL 之前，先经过这个方法
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect("/admin");
            return false;
        }
        return true; // 用户已登录，不进行拦截，即放行
    }

}
