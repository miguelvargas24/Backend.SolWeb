package backend.com.backend.controller;

import backend.com.backend.dto.LoginRequest;
import backend.com.backend.model.User;
import backend.com.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest.getEmail(), loginRequest.getPassword())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestParam String email) {
        System.out.println("=== Endpoint /me llamado ===");
        System.out.println("Buscando usuario por email: " + email);

        var user = userService.findByEmail(email);

        if (user.isPresent()) {
            User foundUser = user.get();
            System.out.println("✓ Usuario encontrado:");
            System.out.println("  - ID: " + foundUser.getIdUser());
            System.out.println("  - Email: " + foundUser.getEmail());
            System.out.println("  - Nombre: " + foundUser.getName());
            System.out.println("  - Activo: " + foundUser.getActive());

            // Verificar que el usuario realmente existe en la DB
            boolean existsById = userService.findById(foundUser.getIdUser()).isPresent();
            System.out.println("  - Verificación por ID: " + (existsById ? "✓ Existe" : "✗ NO EXISTE"));

            return ResponseEntity.ok(foundUser);
        } else {
            System.out.println("✗ Usuario NO encontrado con email: " + email);
            System.out.println("Usuarios disponibles en la base de datos:");
            userService.getAllUsersList().forEach(u -> System.out
                    .println("  - ID: " + u.getIdUser() + ", Email: " + u.getEmail() + ", Nombre: " + u.getName()));
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(java.util.Map.of("error", "Usuario no encontrado", "email", email));
        }
    }
}
