package com.example.auditoria.config;

import com.example.auditoria.usecase.CerrarHallazgoUseCase;
import com.example.auditoria.usecase.ConsultarHallazgoUseCase;
import com.example.auditoria.usecase.ConsultarHistorialUseCase;
import com.example.auditoria.usecase.IniciarRemediacionUseCase;
import com.example.auditoria.usecase.ObtenerDashboardAuditoriaUseCase;
import com.example.auditoria.usecase.ReabrirHallazgoUseCase;
import com.example.auditoria.usecase.RegistrarHallazgoUseCase;
import com.example.auditoria.usecase.impl.CerrarHallazgoService;
import com.example.auditoria.usecase.impl.ConsultarHallazgoService;
import com.example.auditoria.usecase.impl.ConsultarHistorialService;
import com.example.auditoria.usecase.impl.IniciarRemediacionService;
import com.example.auditoria.usecase.impl.ObtenerDashboardAuditoriaService;
import com.example.auditoria.usecase.impl.ReabrirHallazgoService;
import com.example.auditoria.usecase.impl.RegistrarHallazgoService;
import com.example.auditoria.usecase.port.HallazgoRepositoryPort;
import com.example.auditoria.usecase.port.HistorialAuditoriaPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class AuditoriaConfiguration {

    @Bean
    public RegistrarHallazgoUseCase registrarHallazgoUseCase(HallazgoRepositoryPort repo) {
        return new RegistrarHallazgoService(repo);
    }

    // Los tres casos de uso de transicion se ejecutan dentro de una transaccion
    // para que el hallazgo y su registro en la bitacora se guarden juntos.
    // Se hace aqui y no con @Transactional para que usecase/ no dependa de Spring.

    @Bean
    public IniciarRemediacionUseCase iniciarRemediacionUseCase(HallazgoRepositoryPort repo,
            HistorialAuditoriaPort historial, TransactionTemplate tx) {
        IniciarRemediacionService servicio = new IniciarRemediacionService(repo, historial);
        return (id, responsable, fechaLimite, notas) ->
            tx.executeWithoutResult(status -> servicio.ejecutar(id, responsable, fechaLimite, notas));
    }

    @Bean
    public CerrarHallazgoUseCase cerrarHallazgoUseCase(HallazgoRepositoryPort repo,
            HistorialAuditoriaPort historial, TransactionTemplate tx) {
        CerrarHallazgoService servicio = new CerrarHallazgoService(repo, historial);
        return id -> tx.executeWithoutResult(status -> servicio.ejecutar(id));
    }

    @Bean
    public ReabrirHallazgoUseCase reabrirHallazgoUseCase(HallazgoRepositoryPort repo,
            HistorialAuditoriaPort historial, TransactionTemplate tx) {
        ReabrirHallazgoService servicio = new ReabrirHallazgoService(repo, historial);
        return (id, motivo) -> tx.executeWithoutResult(status -> servicio.ejecutar(id, motivo));
    }

    @Bean
    public ConsultarHallazgoUseCase consultarHallazgoUseCase(HallazgoRepositoryPort repo) {
        return new ConsultarHallazgoService(repo);
    }

    @Bean
    public ConsultarHistorialUseCase consultarHistorialUseCase(HallazgoRepositoryPort repo,
            HistorialAuditoriaPort historial) {
        return new ConsultarHistorialService(repo, historial);
    }

    @Bean
    public ObtenerDashboardAuditoriaUseCase obtenerDashboardAuditoriaUseCase(HallazgoRepositoryPort repo) {
        return new ObtenerDashboardAuditoriaService(repo);
    }
}
