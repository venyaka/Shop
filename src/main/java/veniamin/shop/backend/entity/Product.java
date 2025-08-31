package veniamin.shop.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = File.class)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private File image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productCategory", referencedColumnName = "id")
    private ProductCategory productCategory;

    @Column
    private LocalDateTime createdAt;

    @Column
    private Boolean isActive;


    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
