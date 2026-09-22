package edu.compensar.ps2.repo;

import edu.compensar.ps2.model.Transaccion;
import edu.compensar.ps2.model.TipoTransaccion;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface TransaccionRepository {
    Transaccion add(Transaccion t);
    boolean update(Transaccion t);
    boolean delete(int id);
    Optional<Transaccion> findById(int id);
    Collection<Transaccion> findAll();
    List<Transaccion> findByCategoriaId(int categoriaId);
    List<Transaccion> findByTipo(TipoTransaccion tipo);
    List<Transaccion> findByFechaBetween(LocalDate start, LocalDate end);
    List<Transaccion> search(Predicate<Transaccion> filter);
}
