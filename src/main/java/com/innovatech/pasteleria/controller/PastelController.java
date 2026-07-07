package com.innovatech.pasteleria.controller;

import com.innovatech.pasteleria.dto.PastelRequest;
import com.innovatech.pasteleria.dto.PastelResponse;
import com.innovatech.pasteleria.model.Pastel;
import com.innovatech.pasteleria.repository.PastelRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Endpoint de creación y consulta de pasteles (catálogo de la pastelería).
 */
@RestController
@RequestMapping("/api/pasteles")
public class PastelController {

    private final PastelRepository pastelRepository;

    public PastelController(PastelRepository pastelRepository) {
        this.pastelRepository = pastelRepository;
    }

    // Crear un nuevo pastel: nombre, descripción, precio, foto (URL) y stock inicial
    @PostMapping
    public ResponseEntity<PastelResponse> crear(@Valid @RequestBody PastelRequest request) {
        Pastel pastel = new Pastel();
        pastel.setNombre(request.getNombre());
        pastel.setDescripcion(request.getDescripcion());
        pastel.setPrecio(request.getPrecio());
        pastel.setFotoUrl(request.getFotoUrl());
        pastel.setStock(request.getStock());

        Pastel guardado = pastelRepository.save(pastel);
        return ResponseEntity.status(HttpStatus.CREATED).body(new PastelResponse(guardado));
    }

    // Listar todos los pasteles (pantalla principal)
    @GetMapping
    public List<PastelResponse> listar() {
        return pastelRepository.findAll()
                .stream()
                .map(PastelResponse::new)
                .toList();
    }

    @GetMapping("/{id}")
    public PastelResponse obtener(@PathVariable Long id) {
        Pastel pastel = pastelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pastel no encontrado con id " + id));
        return new PastelResponse(pastel);
    }

    @PutMapping("/{id}")
    public PastelResponse actualizar(@PathVariable Long id, @Valid @RequestBody PastelRequest request) {
        Pastel pastel = pastelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pastel no encontrado con id " + id));

        pastel.setNombre(request.getNombre());
        pastel.setDescripcion(request.getDescripcion());
        pastel.setPrecio(request.getPrecio());
        pastel.setFotoUrl(request.getFotoUrl());
        pastel.setStock(request.getStock());

        return new PastelResponse(pastelRepository.save(pastel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!pastelRepository.existsById(id)) {
            throw new NoSuchElementException("Pastel no encontrado con id " + id);
        }
        pastelRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
