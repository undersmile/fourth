package club.guoshizhan.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 整个项目的异常处理
 */
@ControllerAdvice // 拦截所有标注了 @Controller 注解的控制器
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class) // 标识这个方法是可以用来做异常处理的，没有这个注解，异常处理这个功能不起作用
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        logger.error("Request URL : {}，Exception : {}", request.getRequestURL(), e);

        // 如果标识了状态码的页面【例如：有 NotFoundException 类页面】，那就不需要去拦截了，自定义的异常会去做这件事情
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        ModelAndView mv = new ModelAndView();
        mv.addObject("url", request.getRequestURL());
        mv.addObject("exception", e);
        mv.setViewName("error/error");    // 返回到 error 页面
        return mv;
    }

}
