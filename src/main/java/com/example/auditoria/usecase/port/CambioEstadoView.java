package com.example.auditoria.usecase.port;

import java.time.LocalDateTime;

public record CambioEstadoView(String estadoAnterior, String estadoNuevo, String motivo, LocalDateTime fecha) {}
