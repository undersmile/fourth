package club.guoshizhan.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 控制器过滤设置，哪些资源会被拦截，哪些不会
 * 如果继承 WebMvcConfigurationSupport 类，那么页面所有的静态资源都会被拦截
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin") // 这个是跳转到登录页面的 URL ，需要放行
                .excludePathPatterns("/admin/login"); // 如果把这个 URL 拦截了，就无法提交表单了，所以需要放行
    }

}
