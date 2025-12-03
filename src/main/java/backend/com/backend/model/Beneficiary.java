package backend.com.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad Beneficiario
 * Representa tanto Personas Naturales como Asociaciones que reciben ayuda
 */
@Entity
@Table(name = "beneficiaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBeneficiary;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private BeneficiaryType type; // PERSONA_NATURAL, ASOCIACION

    // Para Persona Natural: DNI, Para Asociación: RUC
    @Column(unique = true, nullable = false, length = 11)
    private String documentNumber;

    @Column(nullable = false, length = 200)
    private String name; // Nombre completo o Razón Social

    @Column(length = 300)
    private String address;

    @Column(length = 15)
    private String phone;

    @Column(length = 100)
    private String email;

    // Solo para Asociaciones
    @Column(length = 100)
    private String representativeName;

    @Column(length = 500)
    private String description; // Tipo de ayuda que necesitan

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

    public enum BeneficiaryType {
        PERSONA_NATURAL,
        ASOCIACION
    }
}
