package club.guoshizhan.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect      // 加上这个注解才可以进行切面的操作
@Component   // 加上这个注解主要用于把这个类的对象交给 Spring 管理
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // execution 里面的表达式表示：这个切面需要拦截哪些类。这里拦截的是 controller 包下的所有类
    @Pointcut("execution(* club.guoshizhan.controller.*.*(..))")
    // 这里定义一个普通方法，但是什么事情也不做，通过 @Pointcut 注解使这个方法成为一个切面
    public void log() {}

    // 在切面之前执行一些东西，即只要使用了 controller 里面的任何一个 url 地址
    // 那么在访问其 url 之前必须经过这个 doBefore 方法【真正起作用的是 @Before 注解】
    // doBefore方法中的参数 joinPoint 主要用于获取到访问的类名和参数
    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 获取 url 和 ip 地址
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();

        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        logger.info("Request : {}", new RequestLog(url, ip, classMethod, args));
    }

    // 在请求 controller 里面的方法之后执行一些东西
    @After("log()")
    public void doAfter() {
        // 以下一行代码只供测试使用，测试完后可注释掉
        // logger.info("-------- doAfter --------");
    }

    // 记录返回的内容。returning = "result" 中的 result 来自于方法参数中的 result
    @AfterReturning(returning = "result", pointcut = "log()")
    public void doAfterReturn(Object result) {
        logger.info("Result ==> : {}", result);
    }

    // 定义一个内部类，用于封装 ip 地址、url 等等，方便输出
    private static class RequestLog {
        String url, ip, classMethod;
        Object[] args;

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "{" + "url=" + url + ", ip=" + ip + ", classMethod=" + classMethod + ", args=" + Arrays.toString(args) + '}';
        }
    }

}
