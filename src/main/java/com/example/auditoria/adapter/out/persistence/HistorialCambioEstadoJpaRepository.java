package com.example.auditoria.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialCambioEstadoJpaRepository extends JpaRepository<HistorialCambioEstadoJpaEntity, Long> {
    List<HistorialCambioEstadoJpaEntity> findByHallazgoIdOrderByFechaAsc(String hallazgoId);
}
