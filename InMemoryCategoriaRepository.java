package edu.compensar.ps2.repo;

import edu.compensar.ps2.model.Categoria;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryCategoriaRepository implements CategoriaRepository {
    private final Map<Integer, Categoria> data = new LinkedHashMap<>();
    private final AtomicInteger seq = new AtomicInteger(0);

    @Override
    public Categoria create(String nombre, String descripcion) {
        int id = seq.incrementAndGet();
        Categoria c = new Categoria(id, nombre, descripcion);
        data.put(id, c);
        return c;
    }

    @Override
    public boolean update(int id, String nombre, String descripcion) {
        Categoria c = data.get(id);
        if (c == null) return false;
        c.setNombre(nombre);
        c.setDescripcion(descripcion);
        return true;
    }

    @Override
    public boolean delete(int id) {
        return data.remove(id) != null;
    }

    @Override
    public Optional<Categoria> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<Categoria> findByNombre(String nombre) {
        return data.values().stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    @Override
    public Collection<Categoria> findAll() {
        return Collections.unmodifiableCollection(data.values());
    }
}
