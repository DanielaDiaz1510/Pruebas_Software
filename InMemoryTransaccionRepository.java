package edu.compensar.ps2.repo;

import edu.compensar.ps2.model.Transaccion;
import edu.compensar.ps2.model.TipoTransaccion;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InMemoryTransaccionRepository implements TransaccionRepository {
    private final Map<Integer, Transaccion> data = new LinkedHashMap<>();
    private final AtomicInteger seq = new AtomicInteger(0);

    @Override
    public Transaccion add(Transaccion t) {
        int id = seq.incrementAndGet();
        t.setId(id);
        data.put(id, t);
        return t;
    }

    @Override
    public boolean update(Transaccion t) {
        int id = t.getId();
        if (!data.containsKey(id)) return false;
        data.put(id, t);
        return true;
    }

    @Override
    public boolean delete(int id) {
        return data.remove(id) != null;
    }

    @Override
    public Optional<Transaccion> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Collection<Transaccion> findAll() {
        return Collections.unmodifiableCollection(data.values());
    }

    @Override
    public List<Transaccion> findByCategoriaId(int categoriaId) {
        return new ArrayList<>(data.values().stream()
                .filter(t -> t.getCategoria() != null && t.getCategoria().getId() == categoriaId)
                .collect(Collectors.toList()));
    }

    @Override
    public List<Transaccion> findByTipo(TipoTransaccion tipo) {
        return new ArrayList<>(data.values().stream()
                .filter(t -> t.getTipo() == tipo)
                .collect(Collectors.toList()));
    }

    @Override
    public List<Transaccion> findByFechaBetween(LocalDate start, LocalDate end) {
        return new ArrayList<>(data.values().stream()
                .filter(t -> (t.getFecha().isEqual(start) || t.getFecha().isAfter(start)) &&
                             (t.getFecha().isEqual(end)   || t.getFecha().isBefore(end)))
                .collect(Collectors.toList()));
    }

    @Override
    public List<Transaccion> search(Predicate<Transaccion> filter) {
        List<Transaccion> list = new ArrayList<>();
        for (Transaccion t : data.values()) {
            if (filter.test(t)) list.add(t);
        }
        return list;
    }
}
