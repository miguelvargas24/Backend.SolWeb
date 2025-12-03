package backend.com.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Distribución (Salida de Alimentos)
 * Representa el registro de entrega de alimentos a beneficiarios
 */
@Entity
@Table(name = "distributions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Distribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDistribution;

    @Column(unique = true, nullable = false, length = 50)
    private String distributionCode; // Código único: DIS-2025-0001

    @ManyToOne
    @JoinColumn(name = "beneficiary_id", nullable = false)
    private Beneficiary beneficiary;

    @Column(nullable = false)
    private LocalDate distributionDate;

    @ManyToOne
    @JoinColumn(name = "delivered_by_user_id", nullable = false)
    private User deliveredBy; // Usuario que registra la entrega

    @Column(length = 500)
    private String observations;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private DistributionStatus status = DistributionStatus.DELIVERED;

    @OneToMany(mappedBy = "distribution", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DistributionDetail> details = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum DistributionStatus {
        DELIVERED, // Entregada
        VALIDATED, // Validada
        CANCELLED // Cancelada
    }
}
