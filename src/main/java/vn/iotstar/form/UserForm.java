package vn.iotstar.form;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import vn.iotstar.entity.AppUser.Role;
@Getter @Setter
public class UserForm {
    @NotBlank(message="Vui lòng nhập tên đăng nhập.")
    @Pattern(regexp="[A-Za-z0-9_.-]{3,50}", message="Tên đăng nhập gồm 3–50 chữ không dấu, số, dấu chấm, gạch dưới hoặc gạch ngang.") private String username;
    @NotBlank(message="Vui lòng nhập họ tên.") @Size(max=100, message="Họ tên tối đa 100 ký tự.") private String fullname;
    @NotBlank(message="Vui lòng nhập email.") @Email(message="Email không hợp lệ.") @Size(max=150) private String email;
    @Pattern(regexp="^$|[0-9+]{9,15}", message="Số điện thoại gồm 9–15 chữ số, có thể có dấu +.") private String phone="";
    @Size(max=500) @Pattern(regexp="(?i)^$|https?://[^\\s<>\"]+", message="Ảnh phải là đường dẫn HTTP hoặc HTTPS.") private String imageUrl="";
    @Size(max=64, message="Mật khẩu tối đa 64 ký tự.") private String password="";
    @NotNull(message="Vui lòng chọn quyền hợp lệ.") private Role role=Role.USER;
    private boolean enabled=true;
    private Long version;
}
