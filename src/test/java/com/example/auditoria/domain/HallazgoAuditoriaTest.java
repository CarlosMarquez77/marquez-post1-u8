package com.example.auditoria.domain;

import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.EstadoHallazgo;
import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.domain.valueobject.PlanRemediacion;
import com.example.auditoria.domain.valueobject.Severidad;
import com.example.auditoria.domain.valueobject.TransicionInvalidaException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Prueba del dominio sin @SpringBootTest: solo JUnit
class HallazgoAuditoriaTest {

    private HallazgoAuditoria nuevoHallazgo() {
        return new HallazgoAuditoria(HallazgoId.nuevo(), "Contraseñas por defecto", "Servidor QA",
            "Infraestructura", Severidad.ALTA, LocalDate.of(2026, 8, 1));
    }

    private PlanRemediacion plan() {
        return new PlanRemediacion("Equipo de Infraestructura", LocalDate.of(2026, 8, 20), "Rotar credenciales");
    }

    @Test
    void recorreElCicloCompleto() {
        HallazgoAuditoria hallazgo = nuevoHallazgo();
        assertEquals(EstadoHallazgo.ABIERTO, hallazgo.getEstado());

        hallazgo.iniciarRemediacion(plan());
        assertEquals(EstadoHallazgo.EN_REMEDIACION, hallazgo.getEstado());

        hallazgo.cerrar();
        assertEquals(EstadoHallazgo.CERRADO, hallazgo.getEstado());

        hallazgo.reabrir();
        assertEquals(EstadoHallazgo.REABIERTO, hallazgo.getEstado());
    }

    @Test
    void noSePuedeCerrarSinPlan() {
        HallazgoAuditoria hallazgo = nuevoHallazgo();
        assertThrows(IllegalStateException.class, hallazgo::cerrar);
    }

    @Test
    void noSePuedeReabrirUnHallazgoAbierto() {
        HallazgoAuditoria hallazgo = nuevoHallazgo();
        assertThrows(TransicionInvalidaException.class, hallazgo::reabrir);
    }
}
