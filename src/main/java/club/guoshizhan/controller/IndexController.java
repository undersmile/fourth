package club.guoshizhan.controller;

import club.guoshizhan.service.IBlogService;
import club.guoshizhan.service.ICategoryService;
import club.guoshizhan.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    private IBlogService blogService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ITagService tagService;

    @GetMapping({"/", "/index"})
    public String index(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        // 拿到博客的分页数据，放到 model 里面
        model.addAttribute("page", blogService.listBlog(pageable));
        model.addAttribute("types", categoryService.listCategoryTop(15));
        model.addAttribute("tags", tagService.listTagTop(40));
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(10));
        model.addAttribute("newestBlogs", blogService.listNewestBlogTop(10));
        return "index";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.getAndConvert(id));
        return "front/blog";
    }

    @GetMapping("/about")
    public String about() {
        return "front/about";
    }

    // 浏览器不能写 error 路径，写了会报错 status=999 ，然后就无法实现页面跳转
    @GetMapping("/errors")
    public String errors() {
        return "error/error";
    }

    @GetMapping("/error/500")
    public String error500() {
        return "error/500";
    }

    @GetMapping("/error/404")
    public String error404() {
        return "error/404";
    }

    @GetMapping("/friend")
    public String friend() {
        return "front/friendLinks";
    }

    @GetMapping("/layui")
    public String layui() {
        return "layui/index";
    }

}
