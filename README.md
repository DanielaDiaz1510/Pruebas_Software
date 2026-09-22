# 🧪 Sistema de Gestión Financiera - Pruebas de Software II

Este repositorio contiene el código fuente, la suite de pruebas automatizadas y la documentación metodológica del sistema **Gestión Financiera / Finanzas Console**, desarrollado en el lenguaje **Java** bajo una arquitectura modular por capas (App, Model, Repository, Service y Utility).

El proyecto fue diseñado, refactorizado y verificado aplicando metodologías estrictas de Aseguramiento de la Calidad (QA) y Verificación de Software en la Fundación Universitaria Compensar.

## 📁 Componentes y Evidencias de QA Incluidas

*   **Clases de Lógica y Modelos (`.java`):** Código fuente con arquitectura limpia por capas (FinanzasApp, Transaccion, Categoria, Usuario, etc.).
*   **Clases de Pruebas Unitarias (`Test.java`):** Suite de pruebas con JUnit 5 para la validación de servicios y repositorios.
*   **`pom.xml`:** Archivo de configuración de dependencias de Maven para la automatización de pruebas y compilación.
*   **`Plan de pruebas_Proyecto Pruebas de Software`:** Matriz formal en formato de hoja de cálculo evaluando flujos alternos (montos cero, fechas inválidas y excepciones de negocio).
*   **`Informes Técnicos (PDF/Word)`:** Reportes detallados de las fases de análisis, diseño, refactorización y métricas cuantitativas del proyecto.

## 🎯 Resultados del Aseguramiento de Calidad

*   **Validación de Requerimientos:** Cobertura y verificación de 16 requisitos funcionales críticos (RF01 - RF16) enfocados en la integridad del cálculo de saldos y persistencia de transacciones.
*   **Análisis de Cobertura de Código:** Integración de la herramienta **JaCoCo**, logrando una cobertura global sobresaliente del **91% en instrucciones** y del **68% en ramas**, superando el estándar mínimo requerido del 90%.

## 🛠️ Stack Tecnológico Aplicado
*   **Lenguaje Backend:** Java
*   **Gestión de Dependencias:** Maven
*   **Framework de Pruebas:** JUnit 5
*   **Herramientas de Cobertura:** JaCoCo
