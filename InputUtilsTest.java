package edu.compensar.ps2.util;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class InputUtilsTest {
    @Test
    void leerMontoConReintento() {
        String entradas = "abc\n-5\n10,50\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(entradas.getBytes(StandardCharsets.UTF_8)));
        assertEquals(new java.math.BigDecimal("10.50"), InputUtils.leerMonto(sc));
    }

    @Test
    void leerFechaConReintento() {
        String entradas = "2025/10/11\n2025-10-11\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(entradas.getBytes(StandardCharsets.UTF_8)));
        assertEquals(LocalDate.of(2025,10,11), InputUtils.leerFecha(sc));
    }

    @Test
    void leerEntero() {
        String entradas = "x\n7\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(entradas.getBytes(StandardCharsets.UTF_8)));
        assertEquals(7, InputUtils.leerEntero(sc));
    }

    @Test
    void confirmar() {
        String entradas = "quizas\ns\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(entradas.getBytes(StandardCharsets.UTF_8)));
        assertTrue(InputUtils.confirmar(sc, "¿Seguro?"));
    }

    @Test
    void confirmarFalse() {
        String entradas = "quizas\nn\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(entradas.getBytes(StandardCharsets.UTF_8)));
        assertFalse(InputUtils.confirmar(sc, "¿Seguro?"));
    }

}
