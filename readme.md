# 01-如何构建这个博客项目？

## 完善依赖和包结构
- 第一步：构建一个 Spring Boot 项目
- 第二步：引入相关的依赖
- 第三步：刷新 Maven ，完善包结构【即 BlogApplication 主程序入口】
  
## 编写配置文件
- 新建三个配置文件，application.yml、application-dev.yml、application-prod.yml
- application 是主配置文件，dev 是开发环境下的配置文件，prod 是项目上线之后使用的配置文件，即生产环境下的配置文件
- 编写 application.yml 配置文件，代码如下：
```yaml
spring:
  thymeleaf:
    mode: HTML
  # 指定使用哪一个配置文件，是生产环境还是开发环境
  profiles:
    active: dev
```
- 编写 application-dev.yml 配置文件，代码如下：
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/semantic_blog_final?serverTimezone=GMT%2B8
    username: root
    password: root
  # Jpa 配置
  jpa:
    hibernate:
      ddl-auto: update    # 每一次更新操作都会重新生成表结构
    show-sql: true        # 把 sql 语句输出到控制台

# 日志的配置
logging:
  level:
    root: info    # 指定 Spring Boot 本身的日志级别
    club.guoshizhan: debug    # 指定当前项目 club.guoshizhan 包下的日志级别
  file:
    name: blog-log/blog-dev.log
```
- 编写 application-prod.yml 配置文件，代码如下：
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/graduation_design_blog?serverTimezone=GMT%2B8
    username: root
    password: root
  # Jpa 配置
  jpa:
    hibernate:
      ddl-auto: none
    show-sql: false

# 日志的配置
logging:
  level:
    root: warn
    club.guoshizhan: info
  file:
    name: log/blog-prod.log

# 指定生产环境端口号
server:
  port: 80
```

## 编写日志配置文件
- 在 resources 目录下新建 logback-spring.xml 文件
- 编写 logback-spring.xml 文件，代码如下：
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>
    <!-- 包含 Spring boot 对 logback 日志的默认配置 -->
    <include resource="org/springframework/boot/logging/logback/defaults.xml" />
    <property name="LOG_FILE" value="${LOG_FILE:-${LOG_PATH:-${LOG_TEMP:-${java.io.tmpdir:-/tmp}}}/spring.log}"/>
    <include resource="org/springframework/boot/logging/logback/console-appender.xml" />
    <!-- 重写了 Spring Boot 框架 org/springframework/boot/logging/logback/file-appender.xml 配置 -->
    <appender name="TIME_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <encoder>
            <pattern>${FILE_LOG_PATTERN}</pattern>
        </encoder>
        <file>${LOG_FILE}</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${LOG_FILE}.%d{yyyy-MM-dd}.%i</fileNamePattern>
            <!-- 保留历史日志一个月的时间 -->
            <maxHistory>30</maxHistory>
            <!-- Spring Boot 默认情况下，日志文件 10M 时，会切分日志文件,这样设置日志文件会在 100M 时切分日志 -->
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>10MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
        </rollingPolicy>
    </appender>
    <root level="INFO">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="TIME_FILE" />
    </root>
</configuration>
```

## 启动项目
- 运行 BlogApplication 中的 main 方法来启动项目
- 如果项目成功启动，那么点击控制台的连接跳转到本机的 8080 端口即可
- 博客项目的初步构建完成


# 02-在本项目中，异常是如何处理的？

## 第一步：先定义错误页面：404、500、error 三个错误页面
- 在 resources/templates 目录下新建 error 目录【名字必须是 error ，spring boot 规定了的】
- 在 error 目录下新建上述的三个错误页面，编写相应的前端代码即可
- 到此，完成了错误页面的构建【注意：500 是 Spring Boot 默认回去寻找的页面，error 页面是自定义的错误页面】

## 第二步：自定义全局异常处理器
- 在 guoshizhan 包下新建 handler 包
- 然后在 handler 包下新建 ControllerExceptionHandler 全局异常处理类
- 然后在 handler 包下新建 NotFoundException 异常类，这个异常主要用于返回 404 页面，即指定了状态码的页面
- 到此，全局异常处理搞定

## 第三步：运行项目，测试异常处理器是否起作用
- 在 guoshizhan 包下新建 controller 包
- 在 controller 包下新建 IndexController 类
- 手动制造一个异常，代码如下：
```java
public class IndexController {
    @GetMapping({"/", "/index"})
    public String index() {
        int i = 10 / 0;
        return "index";
    }
}
```
- 运行 BlogApplication 启动类进行测试
- 到此为止，异常处理大功告成

注意：静态资源的导入过程这里不做介绍，自己看 resources 目录就明白了。


# 03-在本项目中，日志是如何处理的？

## 第一步：明白日志需要记录哪些内容
日志主要用于记录：请求 URL、访问者 IP、调用的方法【classMethod】、参数【args】以及返回的内容

## 第二步：创建记录日志的类
- 在 guoshizhan 包下新建 aspect 包
- 在 aspect 包下新建 LogAspect 类，主要对用户的访问进行日志记录
- 这个日志可以记录用户的访问 url、ip、controller 类中的方法名以及输入了哪些参数
- 运行 BlogApplication 启动类访问项目即可
- 到此，记录日志完成。【TIPS：如果有时间，可以把日志记录写到数据库中】


# 04-在本项目中，前端页面是如何处理的？

## 第一步：导入前端页面和静态文件
- 把前端页面放到 resources 下的 templates 目录下
- 把静态资源放到 resources 下的 static 目录下

## 第二步：抽取公共部分
- 抽取公共部分可以简化页面，例如把头部和底部抽取出来。那么如何抽取头部呢？往下看
- 在 templates 目录下新建 fragment 目录
- 在 fragment 目录下新建 front_fragment 页面
- 然后把 header 部分的内容抽取成如下片段。代码如下：
```html
<!-- head 部分 -->
<head th:fragment="head(title)">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title th:replace="${title}">博客页面片段</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <link rel="stylesheet" th:href="@{/css/semantic.css}">
    <link rel="stylesheet" th:href="@{/lib/editormd/css/editormd.min.css}">
    <link rel="stylesheet" th:href="@{/lib/editormd/lib/codemirror/codemirror.min.css}">
    <link rel="stylesheet" th:href="@{/css/typo.css}">
    <link rel="stylesheet" th:href="@{/css/animate.css}">
    <link rel="stylesheet" th:href="@{/lib/prism/prism.css}">
    <link rel="stylesheet" th:href="@{/lib/tocbot/tocbot.css}">
</head>
```
- 接下来是重要的一步：在 index 页面使用定义的片段引入 head 部分。代码如下：
```html
<head th:replace="fragment/front_fragment :: head(~{::title})">
    <title>何年の再遇见-博客首页</title>
</head>
```
- 到此，页面的抽取完成，其他部分的抽取类似，就不在这个地方做解释了。
- 最后，把抽取好的片段应用到各个页面即可。到此，页面部分的处理就 OK 了


# 05-本项目的实体类是如何构建的呢？

## 第一步：先对实体类进行分析
- 首先以 Blog 为中心，分析 Blog 类与其他类的关系
- 比如：Blog 类和 Tag 类，它们的关系是 ==> 多对多的关系
- 把所有关系分析出来之后就进行实体类的构建
- 这里介绍的比较简单，后面会以博客的形式展现出来

## 第二步：构建各种实体类
- 在 guoshizhan 包下新建 PO 包
- 在 PO 包下新建各种实体类。Blog、Tag、Category、User、Comment

## 第三步：启动项目
- 启动项目，即可把刚刚新建的各种实体类自动生成到数据库
- 启动完成之后去数据库查看即可
- 到此，实体类构建完成


# 06-如何实现后台登录？

## 第一步：构建登录页面和后台管理首页
- 在 templates/admin 目录下新建 login 登录页面和 index 后台管理首页
- 在 templates/fragment 目录下新建 admin_fragment 页面，用于抽取后台页面的公共片段
- 启动项目，查看页面效果即可。到此，页面构建完成

## 第二步：编写 service 和 repository
- 在 guoshizhan 包下新建 mapper 包
- 在 mapper 下新建 UserRepository 接口，在这个接口里面添加一个 findByUsernameAndPassword 的方法
- 注意：这个接口需要继承 JpaRepository 接口，而且，方法名字必须是 findByUsernameAndPassword ，因为 Jpa 中，方法命名是有规则的

- 在 guoshizhan 包下新建 service 包
- 在 service 包下新建 IUserService 接口，在这个接口里面添加一个 CheckUser 的抽象方法
- 在 service 包新新建 impl 包
- 在 service/impl 包下新建 UserServiceImpl 类来实现 IUserService 接口
- 到此就完成了 service 和 repository 代码实现。

## 第三步：编写 LoginController 实现登录
- 在 guoshizhan/controller 包下新建 admin 包
- 然后在 admin 包下新建 LoginController 类，用于实现登录操作
- 具体如何实现登录，请自行查阅 LoginController 中的代码
- 编写完成之后，再去 login.html 编写代码，主要是把拿到的后台数据填充到模板中。主要查看 form 表单中的属性值
- 然后再去数据库添加一个用户
- 最后启动项目，进行登录测试，Over！


## 第四步：MD5 加密
- 在 guoshizhan 包下新建 utils 工具包
- 在 utils 包下新建 MD5Utils 加密类，然后把自己的密码放到 main 方法中测试，然后得到一个字符串
- 把这个字符串放到 t_user 表中的 password 中，保存即可
- 然后修改 UserServiceImpl 中的 checkUser 方法即可。
- 最后启动项目，重新测试，搞定！

## 第五步：实现登录拦截器

- 在 guoshizhan 包下新建 interceptor 包
- 在 interceptor 包下新建 LoginInterceptor 类和 WebConfig 类，这两个类用于拦截路径中包含了 admin 的 URL
- 具体如何操作的，请参阅两个类中的代码
- 最后启动项目，重新测试，搞定！


# 07-如何实现博客的分类管理功能？

## 第一步：分类的管理页面
- 在 resources/templates/admin 目录下新建 categories 页面和 categoriesEdit 页面
- 然后使用 Thymeleaf 模板的片段替换其中一些内容
- 替换之后，页面的创建就搞定了

## 第二步：分类列表的分页、新增、修改、删除操作
- 首先在 mapper 包下新建 CategoryRepository 接口
- 然后在 service 包下新建 ICategoryService 接口，然后定义相关的方法
- 然后在 service/impl 包下新建 CategoryServiceImpl 类，用于实现 ICategoryService 接口
- 接下来在 controller 中定义 CategoryController 类，里面进行各种页面控制相关的操作【这个类很重要】
- 最后启动项目，进行测试，搞定！
- 注意：标签管理和分类管理是一样的，所以这里就不再重复记录了

# 08-如何实现博客的管理？

## 第一步：博客的分页查询
- 在 resources/templates/admin 目录下新建 blogs 页面和 blogsEdit 页面
- 然后使用 Thymeleaf 模板的片段替换其中一些内容
- 在 mapper 包下新建 BlogRepository 接口
- 在 guoshizhan 包下新建 VO 包，然后在 VO 包下新建 BlogQuery 类，用于构造条件查询
- 然后在 service 包下新建 IBlogService 接口，且在 service/impl 包下新建 BlogServiceImpl 类
- 然后在 controller 包下新建 BlogController 类
- 启动项目，运行测试，搞定

## 第二步：博客的增删改查
- 按照注释内容查看代码即可，这里不做过多介绍


# 09-如何把数据渲染到前端首页？


# 10-如何实现首页文章图片左右布局？
- 遍历时加迭代器 ===>  th:each="blog,iterStat : ${page.content}
- 对迭代器进行判断 ===>  th:if="${iterStat.count} % 2 == 0 或者 th:if="${iterStat.count} % 2 == 0
- 使文章标题居中显示 ==> center aligned
- 完成


# 11-如何做全局搜索博客？
在 resources/templates/front 目录下新建 search 页面
在 IndexController 中编写 search 相关的 代码


# 12-如何实现博客详情的数据填充？
- 使用 MarkdownUtils 工具类搞定博客的 content 内容
- 使用 th:text="${blog.content}" 来填充内容

## 评论功能
- 评论信息提交与回复功能
- 评论信息列表展示功能
- 管理员回复评论功能

未解决：blogsEdit 页面的分享、评论、转载还没有填充数据

# 国际化内容不做？



# Thymeleaf 语法


# 项目开发过程中遇到一个问题：Request method 'POST' not supported
最终的解决方案就是在 application 配置文件中加入如下配置：
spring:
  mvc:
    hiddenmethod:
      filter:
        enabled: true
