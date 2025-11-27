package cl.ritmolab.ritmolab_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String artist; // Artista del cd o el vinilo

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category type; // cd, vinilo, accesorio
    
    @Column(length = 1000) 
    private String description;
    
    @Column(nullable = false)
    private Double price;
    
    @Column(nullable = false)
    private Integer stock; // cantidad disponible del producto

    private String imageUrl; // URL o ruta de la imagen, usada en el frontend 

    /**
     * Enum para clasificar los productos de la tienda
     */
    public enum Category {
        CD,
        VINILO,
        ACCESORIO
    }
}