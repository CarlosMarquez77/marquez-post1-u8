package com.example.auditoria.usecase;

import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.usecase.port.CambioEstadoView;

import java.util.List;

public interface ConsultarHistorialUseCase {
    List<CambioEstadoView> ejecutar(HallazgoId id);
}
