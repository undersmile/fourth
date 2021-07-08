package club.guoshizhan.controller;

import club.guoshizhan.service.IBlogService;
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
public class SearchController {

    @Autowired
    private IBlogService blogService;

    @GetMapping("/search/query={query}")
    public String getSearch(@PageableDefault(size = 10, sort = {"updateTime"},
            direction = Sort.Direction.DESC) Pageable pageable,
                            @PathVariable String query, Model model) {
        model.addAttribute("page", blogService.listBlog("%" + query + "%", pageable));
        model.addAttribute("query", query);
        return "front/search";
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size = 10, sort = {"updateTime"},
                            direction = Sort.Direction.DESC) Pageable pageable,
                            @RequestParam String query, Model model) {
        model.addAttribute("page", blogService.listBlog("%" + query + "%", pageable));
        model.addAttribute("query", query);
        return "front/search";
    }

}
