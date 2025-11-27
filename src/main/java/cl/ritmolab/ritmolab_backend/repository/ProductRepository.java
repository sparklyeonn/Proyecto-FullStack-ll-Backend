package cl.ritmolab.ritmolab_backend.repository;

import cl.ritmolab.ritmolab_backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}