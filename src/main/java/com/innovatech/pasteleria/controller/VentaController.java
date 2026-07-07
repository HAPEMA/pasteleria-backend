package com.innovatech.pasteleria.controller;

import com.innovatech.pasteleria.dto.VentaRequest;
import com.innovatech.pasteleria.dto.VentaResponse;
import com.innovatech.pasteleria.exception.StockInsuficienteException;
import com.innovatech.pasteleria.model.Pastel;
import com.innovatech.pasteleria.model.Venta;
import com.innovatech.pasteleria.repository.PastelRepository;
import com.innovatech.pasteleria.repository.VentaRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Endpoint de ventas: permite elegir un pastel y una cantidad, descuenta
 * stock y deja registro (con fecha) de lo vendido.
 */
@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaRepository ventaRepository;
    private final PastelRepository pastelRepository;

    public VentaController(VentaRepository ventaRepository, PastelRepository pastelRepository) {
        this.ventaRepository = ventaRepository;
        this.pastelRepository = pastelRepository;
    }

    // Registrar una venta: valida stock disponible, descuenta y guarda con fecha/hora
    @PostMapping
    @Transactional
    public ResponseEntity<VentaResponse> vender(@Valid @RequestBody VentaRequest request) {
        Pastel pastel = pastelRepository.findById(request.getPastelId())
                .orElseThrow(() -> new NoSuchElementException("Pastel no encontrado con id " + request.getPastelId()));

        if (pastel.getStock() < request.getCantidad()) {
            throw new StockInsuficienteException(
                    "Stock insuficiente para \"" + pastel.getNombre() + "\". Disponible: " + pastel.getStock());
        }

        pastel.setStock(pastel.getStock() - request.getCantidad());
        pastelRepository.save(pastel);

        Venta venta = new Venta();
        venta.setPastel(pastel);
        venta.setCantidad(request.getCantidad());
        venta.setPrecioUnitario(pastel.getPrecio());
        venta.setTotal(pastel.getPrecio().multiply(BigDecimal.valueOf(request.getCantidad())));

        Venta guardada = ventaRepository.save(venta);
        return ResponseEntity.status(HttpStatus.CREATED).body(new VentaResponse(guardada));
    }

    // Historial de ventas (vendidos con fecha), más reciente primero
    @GetMapping
    public List<VentaResponse> listar() {
        return ventaRepository.findAllByOrderByFechaVentaDesc()
                .stream()
                .map(VentaResponse::new)
                .toList();
    }
}
