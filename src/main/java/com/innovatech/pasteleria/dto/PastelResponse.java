package com.innovatech.pasteleria.dto;

import com.innovatech.pasteleria.model.Pastel;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class PastelResponse {
    private final Long id;
    private final String nombre;
    private final String descripcion;
    private final BigDecimal precio;
    private final String fotoUrl;
    private final Integer stock;
    private final LocalDateTime fechaCreacion;

    public PastelResponse(Pastel pastel) {
        this.id = pastel.getId();
        this.nombre = pastel.getNombre();
        this.descripcion = pastel.getDescripcion();
        this.precio = pastel.getPrecio();
        this.fotoUrl = pastel.getFotoUrl();
        this.stock = pastel.getStock();
        this.fechaCreacion = pastel.getFechaCreacion();
    }
}
