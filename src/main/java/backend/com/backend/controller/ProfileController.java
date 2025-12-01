package backend.com.backend.controller;

import backend.com.backend.model.Profile;
import backend.com.backend.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier frontend
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public List<Profile> getAllProfiles() {
        return profileService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profile> getProfileById(@PathVariable Long id) {
        return profileService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Profile createProfile(@RequestBody Profile profile) {
        return profileService.save(profile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profile> updateProfile(@PathVariable Long id, @RequestBody Profile profileDetails) {
        return profileService.findById(id)
                .map(existingProfile -> {
                    existingProfile.setName(profileDetails.getName());
                    existingProfile.setDescription(profileDetails.getDescription());

                    // Actualizar permisos si se envían
                    if (profileDetails.getPermissions() != null) {
                        // Nota: Una implementación más robusta manejaría la fusión de permisos
                        // Aquí reemplazamos la lista, lo cual requiere cuidado con orphanRemoval
                        existingProfile.getPermissions().clear();
                        existingProfile.getPermissions().addAll(profileDetails.getPermissions());
                        // Asegurar relación bidireccional
                        existingProfile.getPermissions().forEach(p -> p.setProfile(existingProfile));
                    }

                    return ResponseEntity.ok(profileService.save(existingProfile));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        if (profileService.findById(id).isPresent()) {
            profileService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
