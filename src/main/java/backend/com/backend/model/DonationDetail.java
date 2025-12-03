package backend.com.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad Detalle de Donación
 * Representa cada alimento específico dentro de una donación
 */
@Entity
@Table(name = "donation_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDonationDetail;

    @ManyToOne
    @JoinColumn(name = "donation_id", nullable = false)
    private Donation donation;

    @ManyToOne
    @JoinColumn(name = "food_item_id", nullable = false)
    private FoodItem foodItem;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity; // Cantidad/Peso

    @Column(nullable = false, length = 50)
    private String batchNumber; // Número de lote

    @Column(nullable = false)
    private LocalDate expirationDate; // Fecha de vencimiento

    @Column(length = 300)
    private String observations;
}
