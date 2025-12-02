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
@CrossOrigin(origins = "*") // Para pruebas, luego restringe a tu dominio/IP
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        try {
            User newUser = userService.registerNewUser(
                    request.get("username"),
                    request.get("password")
            );
            return ResponseEntity.ok("Usuario registrado exitosamente como: " + newUser.getRole().name());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.get("username"), request.get("password"))
        );
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); 
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        
        // Corrección: pasar username en lugar de UserDetails
        final String jwt = jwtUtil.generateToken(userDetails.getUsername(), role);

        return ResponseEntity.ok(new AuthResponse(jwt, userDetails.getUsername(), role));
    }
}
