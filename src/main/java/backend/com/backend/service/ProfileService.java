package backend.com.backend.service;

import backend.com.backend.model.Profile;
import backend.com.backend.model.ProfilePermission;
import backend.com.backend.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public List<Profile> findAll() {
        // Solo devuelve perfiles sin cargar permisos (más rápido)
        return profileRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Profile> findById(Long id) {
        return profileRepository.findById(id);
    }

    @Transactional
    public Profile save(Profile profile) {
        // Si el perfil tiene permisos, asegurarse de que la relación bidireccional esté
        // bien establecida
        if (profile.getPermissions() != null) {
            for (ProfilePermission permission : profile.getPermissions()) {
                permission.setProfile(profile);
            }
        }
        return profileRepository.save(profile);
    }

    public void deleteById(Long id) {
        profileRepository.deleteById(id);
    }
}
