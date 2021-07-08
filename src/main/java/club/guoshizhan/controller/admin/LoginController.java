package club.guoshizhan.controller.admin;

import club.guoshizhan.PO.User;
import club.guoshizhan.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private IUserService userService;

    @GetMapping
    public String loginPage() {
        return "admin/login";
    }

    @GetMapping("/index")
    public String index() {
        return "admin/index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session, RedirectAttributes attributes) {
        User user = userService.checkUser(username, password);
        if (user != null) {
            user.setPassword(null); // 这里把密码置为 null ，目的是不让 session 知道密码，这样也就无法在前端页面得到密码，因此更安全
            session.setAttribute("user", user);
            int timeout = session.getServletContext().getSessionTimeout();
            System.out.println("SESSION 过期时间：==> " + timeout);
            return "admin/index";
        }
        // 如果用户名或者密码不正确，那就给出如下提示信息
        attributes.addFlashAttribute("message", "用户名或密码错误！");
        return "redirect:/admin"; // 这里使用了重定向，如果使用 return "admin/login" 这条语句，那么重新登录会出现路径错误的问题
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }

}
