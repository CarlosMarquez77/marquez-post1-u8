package com.example.auditoria.adapter.in.web.dto;

import java.time.LocalDate;

public record IniciarRemediacionRequest(String responsable, LocalDate fechaLimite, String notas) {}
