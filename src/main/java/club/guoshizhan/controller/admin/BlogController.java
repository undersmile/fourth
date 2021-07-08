package club.guoshizhan.controller.admin;

import club.guoshizhan.PO.Blog;
import club.guoshizhan.PO.User;
import club.guoshizhan.VO.BlogQuery;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogsEdit";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private IBlogService service;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ITagService tagService;

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 15, sort = {"updateTime"}, direction = Sort.Direction.DESC)
                                Pageable pageable, BlogQuery blog, Model model) {
        model.addAttribute("types", categoryService.listCategory());
        model.addAttribute("page", service.listBlog(pageable, blog));
        return LIST;
    }

    @GetMapping("/blogsInput")
    public String blogsInput(Model model) {
        setCategoryAndTag(model);
        model.addAttribute("blog", new Blog());
        return INPUT;
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC)
                                 Pageable pageable, BlogQuery blog, Model model) {
        model.addAttribute("page", service.listBlog(pageable, blog));
        return "admin/blogs :: blogList";
    }

    @PostMapping("/blogs")
    public String saveOrUpdatePost(Blog blog, RedirectAttributes attributes, HttpSession session) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setCategory(categoryService.getCategory(blog.getCategory().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b;
        if (blog.getId() == null) {
            // 获取不到博客id，说明是新增博客
            b = service.saveBlog(blog);
        } else {
            // 博客有id，说明是修改博客
            b = service.updateBlog(blog.getId(), blog);
        }
        // 操作之后的提示信息
        if (b == null) {
            attributes.addFlashAttribute("message", "很抱歉哦，操作失败了~~~");
        } else {
            attributes.addFlashAttribute("message", "恭喜您，操作成功了~~~");
        }
        return REDIRECT_LIST;
    }

    // 博客的修改
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        setCategoryAndTag(model);
        Blog blog = service.getBlog(id);
        blog.init();
        model.addAttribute("blog", blog);
        return INPUT;
    }

    // 博客的删除
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        service.deleteBlog(id);
        attributes.addFlashAttribute("message", "恭喜您，删除成功了~~~");
        return REDIRECT_LIST;
    }

    private void setCategoryAndTag(Model model) {
        model.addAttribute("categories", categoryService.listCategory());
        model.addAttribute("tags", tagService.listTag());
    }

}
