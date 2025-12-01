package backend.com.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile_permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProfilePermission;

    @Column(nullable = false)
    private String moduleName;

    private boolean canRead;
    private boolean canCreate;
    private boolean canUpdate;
    private boolean canDelete;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    @JsonIgnore // Evita recursión infinita al serializar a JSON
    private Profile profile;
}
