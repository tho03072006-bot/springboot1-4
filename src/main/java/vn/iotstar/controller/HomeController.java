package vn.iotstar.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.iotstar.service.AdminService;
import java.security.Principal;
@Controller
public class HomeController {
    private final AdminService service;
    public HomeController(AdminService service) { this.service=service; }
    @GetMapping("/") String index(Principal user) { return service.isAdmin(user.getName())?"redirect:/admin":"account"; }
    @GetMapping("/login") String login() { return "login"; }
    @org.springframework.web.bind.annotation.RequestMapping("/denied") @org.springframework.web.bind.annotation.ResponseStatus(org.springframework.http.HttpStatus.FORBIDDEN) String denied() { return "denied"; }
    @GetMapping({"/admin","/admin/","/admin/home"}) String dashboard(Model m) { m.addAttribute("categoryCount",service.categoryCount()); m.addAttribute("userCount",service.userCount()); return "admin/dashboard"; }
}
