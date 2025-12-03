package backend.com.backend.config;

import backend.com.backend.model.Profile;
import backend.com.backend.model.ProfilePermission;
import backend.com.backend.model.User;
import backend.com.backend.repository.ProfileRepository;
import backend.com.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, ProfileRepository profileRepository) {
        return args -> {
            System.out.println("=== Iniciando carga de perfiles fijos ===");

            // ========== PERFIL 1: ADMINISTRADOR ==========
            Profile adminProfile = profileRepository.findByName("Administrador")
                    .orElseGet(() -> {
                        Profile profile = new Profile();
                        profile.setName("Administrador");
                        profile.setDescription("Acceso completo a todos los módulos del sistema");

                        List<ProfilePermission> permissions = new ArrayList<>();

                        // Dashboardddd
                        permissions.add(createPermission("Dashboard", true, true, true, true, profile));

                        // Seguridad
                        permissions.add(createPermission("Seguridad", true, true, true, true, profile));
                        permissions.add(createPermission("Usuarios", true, true, true, true, profile));
                        permissions.add(createPermission("Perfiles", true, true, true, true, profile));

                        // Registros
                        permissions.add(createPermission("Registros", true, true, true, true, profile));
                        permissions.add(createPermission("Donantes", true, true, true, true, profile));
                        permissions.add(createPermission("Beneficiarios", true, true, true, true, profile));
                        permissions.add(createPermission("Alimentos", true, true, true, true, profile));

                        // Operaciones
                        permissions.add(createPermission("Operaciones", true, true, true, true, profile));
                        permissions.add(createPermission("Donaciones", true, true, true, true, profile));
                        permissions.add(createPermission("Distribuciones", true, true, true, true, profile));

                        // Inventario
                        permissions.add(createPermission("Inventario", true, true, true, true, profile));

                        profile.setPermissions(permissions);
                        Profile saved = profileRepository.save(profile);
                        System.out.println("Perfil 'Administrador' creado con " + permissions.size() + " permisos");
                        return saved;
                    });

            // ========== PERFIL 2: SUB ADMINISTRADOR ==========
            Profile subAdminProfile = profileRepository.findByName("Sub Administrador")
                    .orElseGet(() -> {
                        Profile profile = new Profile();
                        profile.setName("Sub Administrador");
                        profile.setDescription("Acceso a todos los módulos excepto Seguridad");

                        List<ProfilePermission> permissions = new ArrayList<>();

                        // Dashboard
                        permissions.add(createPermission("Dashboard", true, true, true, true, profile));

                        // NO tiene acceso a Seguridad

                        // Registros
                        permissions.add(createPermission("Registros", true, true, true, true, profile));
                        permissions.add(createPermission("Donantes", true, true, true, true, profile));
                        permissions.add(createPermission("Beneficiarios", true, true, true, true, profile));
                        permissions.add(createPermission("Alimentos", true, true, true, true, profile));

                        // Operaciones
                        permissions.add(createPermission("Operaciones", true, true, true, true, profile));
                        permissions.add(createPermission("Donaciones", true, true, true, true, profile));
                        permissions.add(createPermission("Distribuciones", true, true, true, true, profile));

                        // Inventario
                        permissions.add(createPermission("Inventario", true, true, true, true, profile));

                        profile.setPermissions(permissions);
                        Profile saved = profileRepository.save(profile);
                        System.out
                                .println("Perfil 'Sub Administrador' creado con " + permissions.size() + " permisos");
                        return saved;
                    });

            // ========== PERFIL 3: TRABAJADOR ==========
            Profile trabajadorProfile = profileRepository.findByName("Trabajador")
                    .orElseGet(() -> {
                        Profile profile = new Profile();
                        profile.setName("Trabajador");
                        profile.setDescription("Acceso solo a Operaciones e Inventario");

                        List<ProfilePermission> permissions = new ArrayList<>();

                        // Solo Operaciones e Inventario
                        permissions.add(createPermission("Operaciones", true, true, true, true, profile));
                        permissions.add(createPermission("Donaciones", true, true, true, true, profile));
                        permissions.add(createPermission("Distribuciones", true, true, true, true, profile));
                        permissions.add(createPermission("Inventario", true, true, true, true, profile));

                        profile.setPermissions(permissions);
                        Profile saved = profileRepository.save(profile);
                        System.out.println("Perfil 'Trabajador' creado con " + permissions.size() + " permisos");
                        return saved;
                    });

            System.out.println("=== Perfiles fijos cargados correctamente ===");

            // Crear usuario de prueba Administrador si no existe
            if (userRepository.findByEmail("admin@bancoalimentos.com").isEmpty()) {
                User user = new User();
                user.setName("Administrador Sistema");
                user.setEmail("admin@bancoalimentos.com");
                user.setPassword("admin123");
                user.setActive(true);
                user.setProfile(adminProfile);
                userRepository.save(user);
                System.out.println("Usuario 'Administrador' creado (admin@bancoalimentos.com / admin123)");
            }

            // Crear usuario de prueba Sub Administrador si no existe
            if (userRepository.findByEmail("subadmin@bancoalimentos.com").isEmpty()) {
                User user = new User();
                user.setName("Sub Administrador Sistema");
                user.setEmail("subadmin@bancoalimentos.com");
                user.setPassword("subadmin123");
                user.setActive(true);
                user.setProfile(subAdminProfile);
                userRepository.save(user);
                System.out.println("Usuario 'Sub Administrador' creado (subadmin@bancoalimentos.com / subadmin123)");
            }

            // Crear usuario de prueba Trabajador si no existe
            if (userRepository.findByEmail("trabajador@bancoalimentos.com").isEmpty()) {
                User user = new User();
                user.setName("Trabajador Sistema");
                user.setEmail("trabajador@bancoalimentos.com");
                user.setPassword("trabajador123");
                user.setActive(true);
                user.setProfile(trabajadorProfile);
                userRepository.save(user);
                System.out.println("Usuario 'Trabajador' creado (trabajador@bancoalimentos.com / trabajador123)");
            }

            System.out.println("=== Inicialización completada ===");
        };
    }

    private ProfilePermission createPermission(String moduleName, boolean canRead,
            boolean canCreate, boolean canUpdate,
            boolean canDelete, Profile profile) {
        ProfilePermission permission = new ProfilePermission();
        permission.setModuleName(moduleName);
        permission.setCanRead(canRead);
        permission.setCanCreate(canCreate);
        permission.setCanUpdate(canUpdate);
        permission.setCanDelete(canDelete);
        permission.setProfile(profile);
        return permission;
    }
}
