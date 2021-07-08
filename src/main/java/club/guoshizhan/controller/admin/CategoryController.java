package club.guoshizhan.controller.admin;

import club.guoshizhan.PO.Category;
import club.guoshizhan.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class CategoryController {

    @Autowired
    private ICategoryService service;

    @GetMapping("/categories")
    public String categories(@PageableDefault(size = 9, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("page", service.listCategory(pageable));
        return "admin/categories";
    }

    @GetMapping("/categories/edit")
    public String input(Model model) {
        model.addAttribute("category", new Category());
        return "admin/categoriesEdit";
    }

    @GetMapping("/categories/{id}/edit")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("category", service.getCategory(id));
        return "admin/categoriesEdit";
    }

    // 添加博客分类
    @PostMapping("/categories/insert")
    public String post(@Valid Category c, BindingResult result, RedirectAttributes attributes) {
        Category category1 = service.getCategoryByName(c.getName());
        if (category1 != null) {
            result.rejectValue("name", "nameError", "不能添加重复的分类!");
        }
        if ("".equals(c.getName().trim())) {
            result.rejectValue("name", "nameError", "分类名称不能为空!");
        }
        if (result.hasErrors()) {
            return "admin/categoriesEdit";
        }
        Category category = service.saveCategory(c);
        if (category == null) {
            attributes.addFlashAttribute("message", "不好意思哦，新增失败！");
        } else {
            attributes.addFlashAttribute("message", "恭喜呀，新增成功！");
        }
        return "redirect:/admin/categories";
    }

    // 更新博客分类
    @PostMapping("/categories/insert/{id}")
    public String editPost(@Valid Category category, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {
        Category category1 = service.getCategoryByName(category.getName());
        if (category1 != null) {
            result.rejectValue("name", "nameError", "不能添加重复的分类!");
        }
        if (result.hasErrors()) {
            return "admin/categoriesEdit";
        }
        Category category2 = service.updateCategory(id, category);
        if (category2 == null) {
            attributes.addFlashAttribute("message", "不好意思哦，更新失败!");
        } else {
            attributes.addFlashAttribute("message", "恭喜呀，更新成功!");
        }
        return "redirect:/admin/categories";
    }

    // 删除博客分类
    @GetMapping("/categories/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        service.deleteCategory(id);
        attributes.addFlashAttribute("message", "删除成功！！！");
        return "redirect:/admin/categories";
    }

}
