package club.guoshizhan.controller.admin;

import club.guoshizhan.mapper.CommentRepository;
import club.guoshizhan.service.ICategoryService;
import club.guoshizhan.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class CommentsController {

    @Autowired
    private CommentRepository repository;

    // 查询评论
    @GetMapping("/comments")
    public String categories(@PageableDefault(size = 15, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("page", repository.findAll(pageable));
        return "admin/comments";
    }

    // 删除评论
    @GetMapping("/comments/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        repository.deleteById(id);
        attributes.addFlashAttribute("message", "恭喜您，评论删除成功了~~~");
        return "redirect:/admin/comments";
    }

}
