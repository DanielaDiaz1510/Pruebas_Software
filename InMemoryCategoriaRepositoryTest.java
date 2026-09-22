package edu.compensar.ps2.repo;

import edu.compensar.ps2.model.Categoria;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryCategoriaRepositoryTest {

    @Test
    void crudCategoria() {
        InMemoryCategoriaRepository repo = new InMemoryCategoriaRepository();
        Categoria c = repo.create("Transporte", "Buses");
        assertTrue(c.getId() > 0);
        assertEquals("Transporte", c.getNombre());

        Optional<Categoria> byId = repo.findById(c.getId());
        assertTrue(byId.isPresent());

        Optional<Categoria> byNombre = repo.findByNombre("transporte");
        assertTrue(byNombre.isPresent());

        boolean upd = repo.update(c.getId(), "Movilidad", "Bus y taxi");
        assertTrue(upd);
        assertEquals("Movilidad", repo.findById(c.getId()).get().getNombre());

        Collection<Categoria> all = repo.findAll();
        assertEquals(1, all.size());

        boolean del = repo.delete(c.getId());
        assertTrue(del);
        assertFalse(repo.findById(c.getId()).isPresent());
    }

}
