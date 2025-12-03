package backend.com.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Entidad Detalle de Distribución
 * Representa cada alimento específico dentro de una distribución
 */
@Entity
@Table(name = "distribution_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDistributionDetail;

    @ManyToOne
    @JoinColumn(name = "distribution_id", nullable = false)
    private Distribution distribution;

    @ManyToOne
    @JoinColumn(name = "food_item_id", nullable = false)
    private FoodItem foodItem;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity; // Cantidad entregada

    @Column(length = 50)
    private String batchNumber; // Número de lote de donde salió

    @Column(length = 300)
    private String observations;
}
