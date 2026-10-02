package com.example.auditoria.usecase.port;

import java.util.List;

public record DashboardAuditoriaView(
    List<ConteoCategoria> porSeveridad,
    List<ConteoCategoria> porEstado,
    List<PromedioCategoria> promedioDiasCierrePorArea
) {}
