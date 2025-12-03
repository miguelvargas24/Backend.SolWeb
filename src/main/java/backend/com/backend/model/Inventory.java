package backend.com.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad Inventario
 * Representa el stock actual de alimentos en tiempo real
 * Se actualiza automáticamente con cada donación (entrada) y distribución
 * (salida)
 */
@Entity
@Table(name = "inventory", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "food_item_id", "batch_number" })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInventory;

    @ManyToOne
    @JoinColumn(name = "food_item_id", nullable = false)
    private FoodItem foodItem;

    @Column(nullable = false, length = 50)
    private String batchNumber; // Número de lote

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal currentQuantity; // Cantidad disponible actual

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal initialQuantity; // Cantidad inicial cuando ingresó

    @Column(nullable = false)
    private LocalDate expirationDate; // Fecha de vencimiento

    @Column(nullable = false)
    private LocalDate entryDate; // Fecha de ingreso al inventario

    @Column(nullable = false)
    private LocalDateTime lastMovementDate; // Última fecha de movimiento

    @ManyToOne
    @JoinColumn(name = "donor_id")
    private Donor donor; // De qué donante vino este lote

    @Column(length = 300)
    private String location; // Ubicación física en almacén (Ej: Estante A3)

    @Column(nullable = false)
    private Boolean active = true; // False si el stock llegó a 0

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.lastMovementDate = LocalDateTime.now();
    }

    /**
     * Método para agregar cantidad al inventario (cuando hay una donación)
     */
    public void addQuantity(BigDecimal quantity) {
        this.currentQuantity = this.currentQuantity.add(quantity);
        this.lastMovementDate = LocalDateTime.now();
    }

    /**
     * Método para restar cantidad del inventario (cuando hay una distribución)
     */
    public void subtractQuantity(BigDecimal quantity) {
        this.currentQuantity = this.currentQuantity.subtract(quantity);
        this.lastMovementDate = LocalDateTime.now();
        if (this.currentQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            this.active = false;
        }
    }
}
