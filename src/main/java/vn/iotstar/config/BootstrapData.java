package vn.iotstar.config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.AppUser;
import vn.iotstar.entity.Category;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.repository.CategoryRepository;
@Configuration
public class BootstrapData {
    @Bean CommandLineRunner bootstrap(UserRepository users, CategoryRepository categories, PasswordEncoder encoder,
        @Value("${app.bootstrap.username}") String name,@Value("${app.bootstrap.password}") String password) {
        return args -> {
            if(users.count()==0) {
                if(password.length()<8) throw new IllegalStateException("ADMIN_PASSWORD cần ít nhất 8 ký tự.");
                var admin=new AppUser(); admin.setUsername(name.toLowerCase(java.util.Locale.ROOT)); admin.setFullname("Quản trị viên");
                admin.setEmail("admin@example.com"); admin.setPasswordHash(encoder.encode(password)); admin.setRole(AppUser.Role.ADMIN);
                users.save(admin);

                seedUser(users,encoder,"nguyenvana","Nguyễn Văn An","vana@example.com","0901234567",true);
                seedUser(users,encoder,"tranthib","Trần Thị Bình","thib@example.com","0902345678",true);
                seedUser(users,encoder,"levanc","Lê Văn Cường","vanc@example.com","0903456789",true);
                seedUser(users,encoder,"phamthid","Phạm Thị Dung","thid@example.com","0904567890",false);
            }
            if(categories.count()==0) {
                seedCategory(categories,"Thực phẩm tươi sống","Rau củ quả, thịt cá, trứng sữa được nhập mới mỗi ngày.","https://placehold.co/300x200/2563eb/ffffff?text=Thuc+Pham",true);
                seedCategory(categories,"Đồ uống","Nước ngọt, nước ép trái cây, trà và cà phê đóng chai.","https://placehold.co/300x200/0f766e/ffffff?text=Do+Uong",true);
                seedCategory(categories,"Gia dụng","Thiết bị và vật dụng phục vụ sinh hoạt gia đình hằng ngày.","https://placehold.co/300x200/b45309/ffffff?text=Gia+Dung",true);
                seedCategory(categories,"Đồ đông lạnh","Thực phẩm cấp đông, bảo quản được trong thời gian dài.","https://placehold.co/300x200/1d4ed8/ffffff?text=Dong+Lanh",true);
                seedCategory(categories,"Văn phòng phẩm","Sổ sách, bút viết và dụng cụ học tập, làm việc.","https://placehold.co/300x200/6d28d9/ffffff?text=VP+Pham",false);
            }
        };
    }
    private void seedUser(UserRepository users,PasswordEncoder encoder,String username,String fullname,String email,String phone,boolean enabled) {
        var u=new AppUser();
        u.setUsername(username); u.setFullname(fullname); u.setEmail(email); u.setPhone(phone);
        u.setPasswordHash(encoder.encode("User@12345")); u.setRole(AppUser.Role.USER); u.setEnabled(enabled);
        users.save(u);
    }
    private void seedCategory(CategoryRepository categories,String name,String description,String imageUrl,boolean active) {
        var c=new Category();
        c.setName(name); c.setDescription(description); c.setImageUrl(imageUrl); c.setActive(active);
        categories.save(c);
    }
}
