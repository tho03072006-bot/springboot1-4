package vn.iotstar.form;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
@Getter @Setter
public class CategoryForm {
    @NotBlank(message="Vui lòng nhập tên danh mục.") @Size(max=100, message="Tên tối đa 100 ký tự.") private String name;
    @Size(max=1000, message="Mô tả tối đa 1000 ký tự.") private String description;
    @Size(max=500, message="Đường dẫn ảnh tối đa 500 ký tự.")
    @Pattern(regexp="(?i)^$|https?://[^\\s<>\"]+", message="Ảnh phải là đường dẫn HTTP hoặc HTTPS.") private String imageUrl="";
    private boolean active=true;
    private Long version;
}
