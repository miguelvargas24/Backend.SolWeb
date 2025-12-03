package backend.com.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad Catálogo de Alimentos
 * Define los tipos de alimentos estandarizados (ej. Arroz, Aceite, Atún)
 */
@Entity
@Table(name = "food_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFoodItem;

    @Column(unique = true, nullable = false, length = 100)
    private String name; // Nombre estandarizado: "Arroz", "Aceite", "Atún"

    @Column(length = 500)
    private String description;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private UnitOfMeasure unitOfMeasure; // KG, LT, UN (Unidad)

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private FoodCategory category; // GRANOS, ENLATADOS, LACTEOS, etc.

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

    public enum UnitOfMeasure {
        KG, // Kilogramo
        LT, // Litro
        UN // Unidad
    }

    public enum FoodCategory {
        GRANOS,
        ENLATADOS,
        LACTEOS,
        ACEITES,
        CARNES,
        VERDURAS,
        FRUTAS,
        BEBIDAS,
        OTROS
    }
}
