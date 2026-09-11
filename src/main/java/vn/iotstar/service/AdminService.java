package vn.iotstar.service;
import vn.iotstar.entity.*;
import vn.iotstar.entity.AppUser.Role;
import vn.iotstar.form.*;
import vn.iotstar.repository.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import jakarta.persistence.criteria.Predicate;
import java.util.*;

@Service @Transactional(readOnly=true)
public class AdminService {
    private final CategoryRepository categories;
    private final UserRepository users;
    private final PasswordEncoder encoder;
    public AdminService(CategoryRepository categories, UserRepository users, PasswordEncoder encoder) {
        this.categories=categories; this.users=users; this.encoder=encoder;
    }
    public long categoryCount() { return categories.count(); }
    public long userCount() { return users.count(); }
    public Category category(long id) { return categories.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)); }
    public AppUser user(long id) { return users.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)); }
    public boolean isAdmin(String username) { return users.findByUsernameIgnoreCase(username).map(u -> u.isEnabled() && u.getRole()==Role.ADMIN).orElse(false); }
    public static String keyword(String q) { return q==null ? "" : q.strip().substring(0, Math.min(100,q.strip().length())); }
    private <T> Specification<T> search(String q, String... fields) {
        return (root, query, cb) -> {
            String pattern="%"+keyword(q).toLowerCase(Locale.ROOT).replace("!","!!").replace("%","!%").replace("_","!_")+"%";
            List<Predicate> tests=new ArrayList<>();
            for (String field: fields) tests.add(cb.like(cb.lower(root.get(field)),pattern,'!'));
            return cb.or(tests.toArray(new Predicate[0]));
        };
    }
    private PageRequest paging(int page, int size) { return PageRequest.of(Math.max(0,Math.min(page,100000)), List.of(5,10,20).contains(size)?size:10,Sort.by("id").descending()); }
    public Page<Category> categories(String q,int page,int size) {
        var spec=this.<Category>search(q,"name","description");
        var result=categories.findAll(spec,paging(page,size));
        return result.getTotalPages()>0 && result.getNumber()>=result.getTotalPages() ? categories.findAll(spec,paging(result.getTotalPages()-1,size)):result;
    }
    public Page<AppUser> users(String q,int page,int size) {
        var spec=this.<AppUser>search(q,"username","fullname","email","phone");
        var result=users.findAll(spec,paging(page,size));
        return result.getTotalPages()>0 && result.getNumber()>=result.getTotalPages() ? users.findAll(spec,paging(result.getTotalPages()-1,size)):result;
    }
    private void version(Long submitted,Long actual) {
        if (!Objects.equals(submitted,actual)) throw new BusinessException("", "Dữ liệu đã thay đổi. Hãy tải lại trang trước khi lưu.");
    }
    @Transactional public Category saveCategory(Long id,CategoryForm f) {
        var c=id==null?new Category():category(id);
        if(id!=null) version(f.getVersion(),c.getVersion());
        if(categories.existsByNameIgnoreCaseAndIdNot(f.getName().strip(),id==null?-1L:id)) throw new BusinessException("name","Tên danh mục đã tồn tại.");
        c.setName(f.getName().strip()); c.setDescription(f.getDescription()); c.setImageUrl(f.getImageUrl()); c.setActive(f.isActive());
        return categories.saveAndFlush(c);
    }
    @Transactional public void deleteCategory(long id) { categories.delete(category(id)); categories.flush(); }
    @Transactional public AppUser saveUser(Long id,UserForm f,String actor) {
        var u=id==null?new AppUser():user(id);
        if(id!=null) version(f.getVersion(),u.getVersion());
        if(id!=null && !u.getUsername().equals(f.getUsername())) throw new BusinessException("username","Không được thay đổi tên đăng nhập.");
        if(users.existsByUsernameIgnoreCaseAndIdNot(f.getUsername(),id==null?-1L:id)) throw new BusinessException("username","Tên đăng nhập đã tồn tại.");
        if(users.existsByEmailIgnoreCaseAndIdNot(f.getEmail().strip(),id==null?-1L:id)) throw new BusinessException("email","Email đã được sử dụng.");
        if(id!=null && u.getUsername().equalsIgnoreCase(actor) && (!f.isEnabled() || f.getRole()!=Role.ADMIN)) throw new BusinessException("","Không thể khóa hoặc hạ quyền tài khoản đang đăng nhập.");
        String password=f.getPassword()==null?"":f.getPassword();
        if((id==null || !password.isEmpty()) && (password.isBlank() || password.length()<8 || password.length()>64)) throw new BusinessException("password","Mật khẩu phải có 8–64 ký tự và không chỉ chứa khoảng trắng.");
        if(!password.isEmpty()) u.setPasswordHash(encoder.encode(password));
        if(id==null) u.setUsername(f.getUsername().toLowerCase(Locale.ROOT));
        u.setFullname(f.getFullname().strip()); u.setEmail(f.getEmail().strip().toLowerCase(Locale.ROOT)); u.setPhone(f.getPhone()); u.setImageUrl(f.getImageUrl()); u.setRole(f.getRole()); u.setEnabled(f.isEnabled());
        return users.saveAndFlush(u);
    }
    @Transactional public void deleteUser(long id,String actor) {
        var u=user(id);
        if(u.getUsername().equalsIgnoreCase(actor)) throw new BusinessException("","Không thể xóa tài khoản đang đăng nhập.");
        users.delete(u); users.flush();
    }
}
