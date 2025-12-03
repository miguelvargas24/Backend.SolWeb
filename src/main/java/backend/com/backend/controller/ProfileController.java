package backend.com.backend.controller;

import backend.com.backend.model.Profile;
import backend.com.backend.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier frontend
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<Profile>> getAllProfiles() {
        // Devuelve solo perfiles sin cargar permisos (optimizado)
        return ResponseEntity.ok(profileService.findAll());
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<Profile> getProfileById(@PathVariable Long id) {
        return profileService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST, PUT, DELETE endpoints eliminados
    // Los perfiles son fijos y se cargan desde DataLoader
    // No se permite crear, editar o eliminar perfiles desde el frontend
}
