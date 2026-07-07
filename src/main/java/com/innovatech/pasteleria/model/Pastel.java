package com.innovatech.pasteleria.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representa un pastel disponible en el catálogo de la pastelería.
 *
 * Nota de diseño: la foto se almacena como URL (fotoUrl) en vez de un binario
 * en la base de datos. Esto evita guardar archivos pesados en MySQL y permite
 * apuntar a una imagen alojada externamente (o en S3 en una futura iteración).
 */
@Entity
@Table(name = "pasteles")
@Getter
@Setter
@NoArgsConstructor
public class Pastel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    // Campo adicional: control de stock, necesario para poder "vender"
    // y para no dejar vender más unidades de las disponibles.
    @Column(nullable = false)
    private Integer stock;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.stock == null) {
            this.stock = 0;
        }
    }
}
