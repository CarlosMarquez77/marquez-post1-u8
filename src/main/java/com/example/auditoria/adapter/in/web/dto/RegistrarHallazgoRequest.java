package com.example.auditoria.adapter.in.web.dto;

import com.example.auditoria.domain.valueobject.Severidad;

import java.time.LocalDate;

public record RegistrarHallazgoRequest(String titulo, String descripcion, String areaResponsable,
                                       Severidad severidad, LocalDate fechaDeteccion) {}
