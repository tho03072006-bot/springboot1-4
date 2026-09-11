# Spring Boot 4 – Quản lý Category và User

CRUD dành cho ADMIN: danh sách, chi tiết, thêm, sửa, xóa; tìm kiếm kết hợp phân trang 5/10/20 dòng. Giao diện JSP/JSTL dùng một SiteMesh Decorator 3 với template Bootstrap, có validation và thông báo kết quả.

## Chạy

- Dùng JDK 26 theo cấu hình dự án và Maven (hoặc `mvnw.cmd`).
- Trong terminal VS Code tại thư mục dự án, chạy `.\run.cmd` để build và khởi động (script tự dùng JDK 26 đã có trên máy). Nhấn Ctrl+C để dừng.
- Hoặc chạy `mvn clean verify`, sau đó `java -jar target/springboot1-4-1.0.war`.
- Mở http://localhost:8081/springboot1-4/.
- Tài khoản local khởi tạo lần đầu: **admin / Admin@12345**. Có thể đặt `ADMIN_USERNAME`, `ADMIN_PASSWORD` trước lần chạy đầu. Mật khẩu lưu dạng BCrypt; đổi mật khẩu tại màn hình sửa User.
- H2 mặc định lưu bền vững ở `data/`. Thư mục này không đưa lên Git.

## SQL Server (tùy chọn)

Tạo database riêng `springboot_admin`, đặt biến `DB_USERNAME`, `DB_PASSWORD`, tùy chọn `DB_URL`, rồi chạy WAR với `--spring.profiles.active=sqlserver`. Hibernate tạo hai bảng `categories`, `app_users`. Không dùng chung database Baitap03.

## Chức năng

- Category: tên duy nhất, mô tả, URL ảnh, trạng thái; tìm theo tên/mô tả.
- User: tên đăng nhập duy nhất, họ tên, email duy nhất, điện thoại, URL ảnh, mật khẩu, quyền ADMIN/USER, trạng thái; tìm theo username/họ tên/email/điện thoại.
- Khi sửa User, tên đăng nhập cố định; mật khẩu để trống nghĩa là giữ nguyên.
- USER không vào được `/admin/**`. Không cho tự xóa, khóa hoặc hạ quyền tài khoản admin đang đăng nhập. Xóa dùng POST và CSRF, có xác nhận trên giao diện.
- Phiên bản bản ghi được kiểm tra để tránh ghi đè dữ liệu đã được người khác sửa.
- SiteMesh cấu hình tại `WEB-INF/sitemesh3.xml`, template chung tại `WEB-INF/decorators/admin.jsp`. JSP dùng include để tương thích Tomcat 11.
- Bootstrap lưu trong `assets/`, không cần CDN khi chạy.

## Kiểm thử

`mvn clean verify` kiểm tra phân quyền, CSRF, CRUD, validation, mật khẩu, tìm kiếm/phân trang và render JSP/SiteMesh bằng HTTP trên Tomcat nhúng với database H2 riêng.

WAR có thể chạy trực tiếp hoặc triển khai trên Tomcat 11. Không khởi động đồng thời hai bản ứng dụng dùng chung cổng hoặc cùng file H2.
