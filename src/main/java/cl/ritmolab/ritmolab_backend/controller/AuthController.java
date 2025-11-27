package cl.ritmolab.ritmolab_backend.controller;

import cl.ritmolab.ritmolab_backend.model.User;
import cl.ritmolab.ritmolab_backend.service.UserService;
import cl.ritmolab.ritmolab_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails; 
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://[IP PUBLICA O EL DOMINIO NO SEE]") // CORS debe ser configurado a la IP del EC2 o dominio
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // manejo de la respuesta del login
    public static class AuthResponse {
        public String jwt;
        public String username;
        public String role;
        
        public AuthResponse(String jwt, String username, String role) {
            this.jwt = jwt;
            this.username = username;
            this.role = role;
        }
    }

    // Endpoint de "Registro"
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        try {
            // rol por defecto es USER: se maneja en UserService
            User newUser = userService.registerNewUser(
                    request.get("username"),
                    request.get("password")
            );
            return ResponseEntity.ok("Usuario registrado exitosamente como: " + newUser.getRole().name());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint de Login (aqui se genera el JWT!!!)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        
        // 1. auntenticacion de Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.get("username"), request.get("password"))
        );
        
        // 2. obtener el principal
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); 
        
        // 3. obtener el rol del usuario de sus autoridades (puede ser admin o user)
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        
        // 4. se genera el Token
        final String jwt = jwtUtil.generateToken(userDetails, role);

        // 5. devuelve el JWT y el rol al frontend
        return ResponseEntity.ok(new AuthResponse(jwt, userDetails.getUsername(), role));
    }
}