package cl.ritmolab.ritmolab_backend.repository;

import cl.ritmolab.ritmolab_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; 
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Busca al usuario por su nombre de usuario (el username)
     * Metodo crucial para el login y la validación de jwt
     * * @param username es el nombre de usuario a buscar (debe ser unico)
     * @return es un objeto Optional que contiene el User si existe
     */
    Optional<User> findByUsername(String username);
}