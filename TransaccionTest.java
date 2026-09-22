package edu.compensar.ps2.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransaccionTest {

    @Test
    void ingresoYToStringYEquals() {
        Categoria cat = new Categoria(7, "Salario", "Mensual");
        Ingreso ing = new Ingreso(0, new BigDecimal("1500.00"),
                LocalDate.of(2025, 10, 1), "Pago", cat);

        assertEquals(TipoTransaccion.INGRESO, ing.getTipo());
        assertEquals(new BigDecimal("1500.00"), ing.getMonto());
        assertEquals(LocalDate.of(2025, 10, 1), ing.getFecha());
        assertEquals("Pago", ing.getDescripcion());
        assertEquals(cat, ing.getCategoria());

        ing.setMonto(new BigDecimal("1600.00"));
        ing.setFecha(LocalDate.of(2025, 10, 2));
        ing.setDescripcion("Pago actualizado");
        ing.setCategoria(new Categoria(8, "Bono", "Bono"));
        assertEquals(new BigDecimal("1600.00"), ing.getMonto());
        assertEquals(LocalDate.of(2025, 10, 2), ing.getFecha());
        assertEquals("Pago actualizado", ing.getDescripcion());
        assertEquals("Bono", ing.getCategoria().getNombre());

        String s = ing.toString();
        assertTrue(s.contains("+"));
        assertTrue(s.contains("1600.00"));
    }

    @Test
    void gastoToString() {
        Gasto g = new Gasto(0, new BigDecimal("300.50"),
                LocalDate.of(2025, 10, 3), "Mercado", null);
        assertEquals(TipoTransaccion.GASTO, g.getTipo());
        String s = g.toString();
        assertTrue(s.contains("-"));
        assertTrue(s.contains("300.50"));
    }

}
