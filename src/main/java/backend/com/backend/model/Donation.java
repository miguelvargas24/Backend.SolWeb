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
 * Entidad Donación (Entrada de Alimentos)
 * Representa el registro de ingreso de alimentos al almacén
 */
@Entity
@Table(name = "donations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDonation;

    @Column(unique = true, nullable = false, length = 50)
    private String donationCode; // Código único: DON-2025-0001

    @ManyToOne
    @JoinColumn(name = "donor_id", nullable = false)
    private Donor donor;

    @Column(nullable = false)
    private LocalDate donationDate;

    @ManyToOne
    @JoinColumn(name = "received_by_user_id", nullable = false)
    private User receivedBy; // Usuario que registra la donación

    @Column(length = 500)
    private String observations;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private DonationStatus status = DonationStatus.RECEIVED;

    @OneToMany(mappedBy = "donation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DonationDetail> details = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum DonationStatus {
        RECEIVED, // Recibida
        VALIDATED, // Validada
        CANCELLED // Cancelada
    }
}
