package com.innovatech.pasteleria.dto;

import com.innovatech.pasteleria.model.Venta;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class VentaResponse {
    private final Long id;
    private final Long pastelId;
    private final String pastelNombre;
    private final Integer cantidad;
    private final BigDecimal precioUnitario;
    private final BigDecimal total;
    private final LocalDateTime fechaVenta;

    public VentaResponse(Venta venta) {
        this.id = venta.getId();
        this.pastelId = venta.getPastel().getId();
        this.pastelNombre = venta.getPastel().getNombre();
        this.cantidad = venta.getCantidad();
        this.precioUnitario = venta.getPrecioUnitario();
        this.total = venta.getTotal();
        this.fechaVenta = venta.getFechaVenta();
    }
}
