package club.guoshizhan.controller;

import club.guoshizhan.PO.Category;
import club.guoshizhan.VO.BlogQuery;
import club.guoshizhan.service.IBlogService;
import club.guoshizhan.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
// 类名最好不要重名
public class CategoryShowController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IBlogService blogService;

    @GetMapping("/categories/{id}")
    public String categories(@PageableDefault(size = 6, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                             @PathVariable Long id, Model model) {
        // 1000 代表查询 10000 个分类，实际上根本没有这么多分类。写 1000 目的就是查询所有的分类。当然也可以另写方法进行查询
        List<Category> categories = categoryService.listCategoryTop(10000);
        if (id == -1) {
            // 当第一次进入到分类页面，路径上 id=-1，然后我们拿到第一个分类的 id 给当前 id 赋值
            id = categories.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        model.addAttribute("types", categories);
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        model.addAttribute("activeTypeId", id);    // 当前选中的分类 id，即有颜色的分类的 id
        return "front/categories";
    }

}
