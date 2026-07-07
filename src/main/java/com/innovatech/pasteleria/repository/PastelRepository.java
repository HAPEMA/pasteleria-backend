package com.innovatech.pasteleria.repository;

import com.innovatech.pasteleria.model.Pastel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PastelRepository extends JpaRepository<Pastel, Long> {
}
