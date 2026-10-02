package com.example.auditoria.adapter.out.persistence;

import com.example.auditoria.domain.valueobject.EstadoHallazgo;
import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.usecase.port.CambioEstadoView;
import com.example.auditoria.usecase.port.HistorialAuditoriaPort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class HistorialAuditoriaAdapter implements HistorialAuditoriaPort {

    private final HistorialCambioEstadoJpaRepository jpa;

    public HistorialAuditoriaAdapter(HistorialCambioEstadoJpaRepository jpa) { this.jpa = jpa; }

    @Override
    public void registrar(HallazgoId hallazgoId, EstadoHallazgo anterior, EstadoHallazgo nuevo, String motivo) {
        HistorialCambioEstadoJpaEntity e = new HistorialCambioEstadoJpaEntity();
        e.setHallazgoId(hallazgoId.toString());
        e.setEstadoAnterior(anterior);
        e.setEstadoNuevo(nuevo);
        e.setMotivo(motivo);
        e.setFecha(LocalDateTime.now());
        jpa.save(e); // registro append-only: nunca se actualiza ni se elimina
    }

    @Override
    public List<CambioEstadoView> listarPorHallazgo(HallazgoId hallazgoId) {
        return jpa.findByHallazgoIdOrderByFechaAsc(hallazgoId.toString()).stream()
            .map(e -> new CambioEstadoView(
                e.getEstadoAnterior().toString(), e.getEstadoNuevo().toString(), e.getMotivo(), e.getFecha()))
            .toList();
    }
}
