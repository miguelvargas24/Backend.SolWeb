package backend.com.backend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO para crear donaciones desde el frontend
 */
@Data
public class DonationRequest {
    private Long donor; // ID del donante
    private LocalDate donationDate;
    private Long receivedBy; // ID del usuario
    private String observations;
    private String status;
    private List<DonationDetailRequest> details;

    @Data
    public static class DonationDetailRequest {
        private Long foodItem; // ID del alimento
        private Integer quantity;
        private String batchNumber;
        private LocalDate expirationDate;
        private String observations;
    }
}
