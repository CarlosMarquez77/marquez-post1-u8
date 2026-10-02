package com.example.auditoria.usecase.impl;

import com.example.auditoria.usecase.ObtenerDashboardAuditoriaUseCase;
import com.example.auditoria.usecase.port.DashboardAuditoriaView;
import com.example.auditoria.usecase.port.HallazgoRepositoryPort;

public class ObtenerDashboardAuditoriaService implements ObtenerDashboardAuditoriaUseCase {

    private final HallazgoRepositoryPort repo;

    public ObtenerDashboardAuditoriaService(HallazgoRepositoryPort repo) {
        this.repo = repo;
    }

    @Override
    public DashboardAuditoriaView ejecutar() {
        return new DashboardAuditoriaView(
            repo.contarPorSeveridad(), repo.contarPorEstado(), repo.promedioDiasCierrePorArea());
    }
}
