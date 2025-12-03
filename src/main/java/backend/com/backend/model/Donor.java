package backend.com.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad Donante (Empresa)
 * Representa a las empresas aliadas que donan alimentos (ej. Plaza Vea, KFC)
 */
@Entity
@Table(name = "donors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Donor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDonor;

    @Column(unique = true, nullable = false, length = 11)
    private String ruc;

    @Column(nullable = false, length = 200)
    private String businessName; // Razón Social

    @Column(length = 300)
    private String address;

    @Column(length = 100)
    private String contactName;

    @Column(length = 15)
    private String contactPhone;

    @Column(length = 100)
    private String contactEmail;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
