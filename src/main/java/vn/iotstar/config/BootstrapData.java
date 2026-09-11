package vn.iotstar.config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.AppUser;
import vn.iotstar.repository.UserRepository;
@Configuration
public class BootstrapData {
    @Bean CommandLineRunner bootstrap(UserRepository users,PasswordEncoder encoder,
        @Value("${app.bootstrap.username}") String name,@Value("${app.bootstrap.password}") String password) {
        return args -> {
            if(users.count()==0) {
                if(password.length()<8) throw new IllegalStateException("ADMIN_PASSWORD cần ít nhất 8 ký tự.");
                var admin=new AppUser(); admin.setUsername(name.toLowerCase(java.util.Locale.ROOT)); admin.setFullname("Quản trị viên");
                admin.setEmail("admin@example.com"); admin.setPasswordHash(encoder.encode(password)); admin.setRole(AppUser.Role.ADMIN);
                users.save(admin);
            }
        };
    }
}
