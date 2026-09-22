package edu.compensar.ps2.service;

import edu.compensar.ps2.model.Categoria;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class FinanzasServiceTest {

    @Test
    void gettersSettersEqualsHashToString() {
        Categoria c1 = new Categoria(1, "Comida", "Alimentos");
        Categoria c2 = new Categoria(1, "Otro", "Desc");

        assertEquals(1, c1.getId());
        assertEquals("Comida", c1.getNombre());
        assertEquals("Alimentos", c1.getDescripcion());

        c1.setNombre("Alimentación");
        c1.setDescripcion("Gastos de comida");
        assertEquals("Alimentación", c1.getNombre());
        assertEquals("Gastos de comida", c1.getDescripcion());

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());

        String s = c1.toString();
        assertTrue(s.contains("[1]"));
        assertTrue(s.toLowerCase().contains("alimentación"));
    }

}
