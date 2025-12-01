package backend.com.backend.config;

import backend.com.backend.model.Profile;
import backend.com.backend.model.ProfilePermission;
import backend.com.backend.model.User;
import backend.com.backend.repository.ProfileRepository;
import backend.com.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, ProfileRepository profileRepository) {
        return args -> {
            // 1. Crear Perfil Administrador si no existe
            Profile adminProfile = profileRepository.findByName("Administrador")
                    .orElseGet(() -> {
                        Profile profile = new Profile();
                        profile.setName("Administrador");
                        profile.setDescription("Perfil con acceso total al sistema");

                        // Crear permisos para el módulo de USUARIOS (ejemplo)
                        ProfilePermission permUsuarios = new ProfilePermission();
                        permUsuarios.setModuleName("USUARIOS");
                        permUsuarios.setCanRead(true);
                        permUsuarios.setCanCreate(true);
                        permUsuarios.setCanUpdate(true);
                        permUsuarios.setCanDelete(true);
                        permUsuarios.setProfile(profile);

                        profile.setPermissions(List.of(permUsuarios));

                        return profileRepository.save(profile);
                    });

            // 2. Crear Usuario Almendra si no existe
            if (userRepository.findByEmail("almendra@gmail.com").isEmpty()) {
                User user = new User();
                user.setName("Almendra Terrones Perez");
                user.setEmail("almendra@gmail.com");
                user.setPassword("123456"); // Nota: En producción esto debería estar encriptado
                user.setActive(true);
                user.setProfile(adminProfile);

                userRepository.save(user);
                System.out.println("Usuario 'Almendra' creado con perfil 'Administrador'");
            }
        };
    }
}
