package edu.compensar.ps2.repo;

import edu.compensar.ps2.model.Categoria;
import java.util.Collection;
import java.util.Optional;

public interface CategoriaRepository {
    Categoria create(String nombre, String descripcion);
    boolean update(int id, String nombre, String descripcion);
    boolean delete(int id);
    Optional<Categoria> findById(int id);
    Optional<Categoria> findByNombre(String nombre);
    Collection<Categoria> findAll();
}
