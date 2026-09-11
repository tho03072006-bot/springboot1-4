package vn.iotstar.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity @Table(name="app_users", uniqueConstraints={@UniqueConstraint(columnNames="username"), @UniqueConstraint(columnNames="email")})
@Getter @Setter
public class AppUser {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, length=50, updatable=false) private String username;
    @org.hibernate.annotations.Nationalized
    @Column(nullable=false, length=100) private String fullname;
    @Column(nullable=false, length=150) private String email;
    @Column(length=15) private String phone;
    @Column(length=500) private String imageUrl;
    @Column(nullable=false, length=100) private String passwordHash;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=10) private Role role=Role.USER;
    @Column(nullable=false) private boolean enabled=true;
    @Version private Long version;
    public AppUser() { }
    public enum Role { ADMIN, USER }
}
