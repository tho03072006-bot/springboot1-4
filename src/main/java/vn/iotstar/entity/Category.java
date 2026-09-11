package vn.iotstar.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity @Table(name="categories", uniqueConstraints=@UniqueConstraint(columnNames="name"))
@Getter @Setter
public class Category {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @org.hibernate.annotations.Nationalized
    @Column(nullable=false, length=100) private String name;
    @org.hibernate.annotations.Nationalized
    @Column(length=1000) private String description;
    @Column(length=500) private String imageUrl;
    @Column(nullable=false) private boolean active=true;
    @Version private Long version;
    public Category() { }
}
