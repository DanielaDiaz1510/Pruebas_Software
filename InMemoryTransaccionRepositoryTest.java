package edu.compensar.ps2.repo;

import edu.compensar.ps2.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTransaccionRepositoryTest {

    @Test
    void addUpdateDeleteFind() {
        InMemoryTransaccionRepository repo = new InMemoryTransaccionRepository();
        Categoria cat = new Categoria(5, "Comida", "Alimentos");
        Ingreso ing = new Ingreso(0, new BigDecimal("100.00"),
                LocalDate.of(2025, 10, 10), "Pago", cat);
        Transaccion t = repo.add(ing);
        assertTrue(t.getId() > 0);

        int id = t.getId();
        Optional<Transaccion> byId = repo.findById(id);
        assertTrue(byId.isPresent());

        t.setDescripcion("Pago parcial");
        assertTrue(repo.update(t));
        assertEquals("Pago parcial", repo.findById(id).get().getDescripcion());

        assertTrue(repo.delete(id));
        assertFalse(repo.findById(id).isPresent());
    }

    @Test
    void queries() {
        InMemoryTransaccionRepository repo = new InMemoryTransaccionRepository();
        Categoria comida = new Categoria(1, "Comida", "");
        Categoria transporte = new Categoria(2, "Transporte", "");

        repo.add(new Gasto(0, new BigDecimal("10.00"), LocalDate.of(2025,10,1), "pan", comida));
        repo.add(new Gasto(0, new BigDecimal("20.00"), LocalDate.of(2025,10,5), "bus", transporte));
        repo.add(new Ingreso(0, new BigDecimal("300.00"), LocalDate.of(2025,10,6), "salario", null));

        List<Transaccion> porCat = repo.findByCategoriaId(1);
        assertEquals(1, porCat.size());

        List<Transaccion> gastos = repo.findByTipo(TipoTransaccion.GASTO);
        assertEquals(2, gastos.size());

        List<Transaccion> rango = repo.findByFechaBetween(LocalDate.of(2025,10,2), LocalDate.of(2025,10,6));
        assertEquals(2, rango.size());

        List<Transaccion> search = repo.search(t -> t.getDescripcion().contains("bus"));
        assertEquals(1, search.size());
    }

}
