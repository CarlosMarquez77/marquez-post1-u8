# Post-contenido — Unidad 8: Patrones Arquitectónicos II

## Descripción
Repositorio del post-contenido de la Unidad 8 de Patrones de Diseño
de Software — Sexto Semestre. Sistema de seguimiento de hallazgos
de auditoria interna implementado con Clean Architecture (Parte 1)
y extendido con dashboard agregado y bitacora de trazabilidad
(Parte 2), sobre el mismo proyecto Spring Boot.

## Parte 1 — Clean Architecture (Hallazgos de Auditoria)
El proyecto organiza los cuatro circulos concentricos: Entities
(domain/, con el Aggregate Root HallazgoAuditoria y su maquina de
estados EstadoHallazgo), Use Cases (usecase/, con los puertos y sus
implementaciones), Interface Adapters (adapter/, con
HallazgoController y HallazgoRepositoryAdapter) y Frameworks &
Drivers (Spring Boot + JPA). La dependencia del codigo siempre
apunta hacia adentro, hacia domain/.

```
src/main/java/com/example/auditoria/
├── domain/
│   ├── entity/
│   │   └── HallazgoAuditoria.java       ← Aggregate Root (Círculo Entities)
│   └── valueobject/
│       ├── HallazgoId.java
│       ├── Severidad.java
│       ├── EstadoHallazgo.java
│       ├── PlanRemediacion.java
│       └── TransicionInvalidaException.java
├── usecase/                             ← Círculo Use Cases
│   ├── RegistrarHallazgoUseCase.java, IniciarRemediacionUseCase.java,
│   │   CerrarHallazgoUseCase.java, ReabrirHallazgoUseCase.java, ConsultarHallazgoUseCase.java
│   ├── port/
│   │   └── HallazgoRepositoryPort.java  ← Puerto de salida
│   └── impl/                            ← Una clase por caso de uso
├── adapter/                             ← Círculo Interface Adapters
│   ├── in/web/
│   │   ├── HallazgoController.java
│   │   └── dto/
│   └── out/persistence/
│       ├── HallazgoJpaEntity.java
│       ├── HallazgoJpaRepository.java
│       └── HallazgoRepositoryAdapter.java
├── config/
│   └── AuditoriaConfiguration.java      ← Wiring explícito
└── AuditoriaHallazgosApplication.java
```

Los paquetes domain/ y usecase/ no importan Spring ni JPA. La prueba
HallazgoAuditoriaTest instancia el agregado y lo hace transicionar solo
con JUnit, sin @SpringBootTest.

## Decisiones de diseño
1. Severidad como enum simple vs. EstadoHallazgo como enum con
   maquina de estados — EstadoHallazgo tiene una regla de negocio real:
   qué transiciones son válidas (ABIERTO → EN_REMEDIACION → CERRADO →
   REABIERTO → EN_REMEDIACION). Por eso tiene el método
   puedeTransicionarA y la regla queda junto al valor. Severidad es solo
   una clasificación de cuatro valores sin reglas propias: ninguna
   severidad es "más válida" que otra y no hay transiciones entre
   ellas, así que darle métodos sería agregar comportamiento que nadie
   usa. Se habría preferido lo contrario si existiera una regla propia
   de la severidad, por ejemplo un plazo máximo de remediación según el
   nivel.
2. PlanRemediacion como Value Object embebido vs. agregado separado
   — Un hallazgo no puede pasar a EN_REMEDIACION sin un plan válido ni
   cerrarse sin tener uno. Esa invariante se debe cumplir siempre en la
   misma transacción. Según el criterio de límite de consistencia
   transaccional de los Agregados (Sección 3.3 de la guía), lo que debe
   ser consistente de inmediato va dentro del mismo agregado. Por eso
   PlanRemediacion es un record inmutable dentro de HallazgoAuditoria y
   se guarda en la misma tabla. Como agregado separado, con su propio
   repositorio, podría existir un momento en que el hallazgo esté
   EN_REMEDIACION y el plan todavía no exista.

## Cómo ejecutar
```
$ mvn spring-boot:run
```

## Herramientas utilizadas
- Java 17, Spring Boot 3.x, Spring Data JPA, H2
- Apache Maven, Postman/curl, Git, GitHub
