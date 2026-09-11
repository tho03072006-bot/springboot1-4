package vn.iotstar.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import jakarta.validation.Valid;
import java.security.Principal;
import vn.iotstar.form.*;
import vn.iotstar.entity.AppUser.Role;
import vn.iotstar.service.*;
@Controller @RequestMapping("/admin")
public class AdminController {
    private final AdminService service;
    public AdminController(AdminService service) { this.service=service; }
    @ModelAttribute("roles") Role[] roles() { return Role.values(); }
    @GetMapping("/categories") String categories(@RequestParam(defaultValue="") String q,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="10") int size,Model m) {
        m.addAttribute("result",service.categories(q,page,size)); m.addAttribute("q",AdminService.keyword(q)); m.addAttribute("listPath","/admin/categories"); return "admin/categories/list";
    }
    @GetMapping("/categories/new") String categoryNew(Model m) { m.addAttribute("form",new CategoryForm()); return "admin/categories/form"; }
    @GetMapping("/categories/{id}") String categoryDetail(@PathVariable long id,Model m) { m.addAttribute("item",service.category(id)); return "admin/categories/detail"; }
    @GetMapping("/categories/{id}/edit") String categoryEdit(@PathVariable long id,Model m) {
        var c=service.category(id); var f=new CategoryForm(); f.setName(c.getName()); f.setDescription(c.getDescription()); f.setImageUrl(c.getImageUrl()); f.setActive(c.isActive()); f.setVersion(c.getVersion()); m.addAttribute("id",id); m.addAttribute("form",f); return "admin/categories/form";
    }
    @PostMapping({"/categories","/categories/{id}"}) String categorySave(@PathVariable(required=false) Long id,@Valid @ModelAttribute("form") CategoryForm f,BindingResult errors,Model m,RedirectAttributes flash) {
        if(id!=null) service.category(id);
        m.addAttribute("id",id);
        if(!errors.hasErrors()) try { var saved=service.saveCategory(id,f); flash.addFlashAttribute("success","Đã lưu danh mục."); return "redirect:/admin/categories/"+saved.getId(); }
        catch(BusinessException e) { reject(errors,e); }
        catch(DataIntegrityViolationException e) { errors.reject("duplicate","Tên danh mục đã tồn tại."); }
        catch(OptimisticLockingFailureException e) { errors.reject("conflict","Dữ liệu đã thay đổi. Vui lòng tải lại trang."); }
        return "admin/categories/form";
    }
    @PostMapping("/categories/{id}/delete") String categoryDelete(@PathVariable long id,RedirectAttributes flash) { service.deleteCategory(id); flash.addFlashAttribute("success","Đã xóa danh mục."); return "redirect:/admin/categories"; }
    @GetMapping("/users") String users(@RequestParam(defaultValue="") String q,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="10") int size,Model m) {
        m.addAttribute("result",service.users(q,page,size)); m.addAttribute("q",AdminService.keyword(q)); m.addAttribute("listPath","/admin/users"); return "admin/users/list";
    }
    @GetMapping("/users/new") String userNew(Model m) { m.addAttribute("form",new UserForm()); return "admin/users/form"; }
    @GetMapping("/users/{id}") String userDetail(@PathVariable long id,Model m) { m.addAttribute("item",service.user(id)); return "admin/users/detail"; }
    @GetMapping("/users/{id}/edit") String userEdit(@PathVariable long id,Model m) {
        var u=service.user(id); var f=new UserForm(); f.setUsername(u.getUsername()); f.setFullname(u.getFullname()); f.setEmail(u.getEmail()); f.setPhone(u.getPhone()); f.setImageUrl(u.getImageUrl()); f.setRole(u.getRole()); f.setEnabled(u.isEnabled()); f.setVersion(u.getVersion()); m.addAttribute("form",f); m.addAttribute("id",id); return "admin/users/form";
    }
    @PostMapping({"/users","/users/{id}"}) String userSave(@PathVariable(required=false) Long id,@Valid @ModelAttribute("form") UserForm f,BindingResult errors,Model m,RedirectAttributes flash,Principal actor) {
        if(id!=null) service.user(id);
        m.addAttribute("id",id);
        if(!errors.hasErrors()) try { var saved=service.saveUser(id,f,actor.getName()); flash.addFlashAttribute("success","Đã lưu người dùng."); return "redirect:/admin/users/"+saved.getId(); }
        catch(BusinessException e) { reject(errors,e); }
        catch(DataIntegrityViolationException e) { errors.reject("duplicate","Tên đăng nhập hoặc email đã tồn tại."); }
        catch(OptimisticLockingFailureException e) { errors.reject("conflict","Dữ liệu đã thay đổi. Vui lòng tải lại trang."); }
        f.setPassword(""); return "admin/users/form";
    }
    @PostMapping("/users/{id}/delete") String userDelete(@PathVariable long id,Principal actor,RedirectAttributes flash) {
        try { service.deleteUser(id,actor.getName()); flash.addFlashAttribute("success","Đã xóa người dùng."); }
        catch(BusinessException e) { flash.addFlashAttribute("failure",e.getMessage()); }
        return "redirect:/admin/users";
    }
    private void reject(BindingResult errors,BusinessException e) { if(e.getField().isEmpty()) errors.reject("business",e.getMessage()); else errors.rejectValue(e.getField(),"business",e.getMessage()); }
}
