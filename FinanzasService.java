package edu.compensar.ps2.service;

import edu.compensar.ps2.model.*;
import edu.compensar.ps2.repo.CategoriaRepository;
import edu.compensar.ps2.repo.TransaccionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class FinanzasService {
    private final CategoriaRepository categorias;
    private final TransaccionRepository transacciones;

    public FinanzasService(CategoriaRepository categorias, TransaccionRepository transacciones) {
        this.categorias = categorias;
        this.transacciones = transacciones;
    }

    
    public Categoria crearCategoria(String nombre, String descripcion) {
        Objects.requireNonNull(nombre, "nombre");
        return categorias.create(nombre.trim(), descripcion == null ? "" : descripcion.trim());
    }

    public boolean editarCategoria(int id, String nombre, String descripcion) {
        return categorias.update(id, nombre, descripcion);
    }

    public boolean eliminarCategoria(int id) {
        return categorias.delete(id);
    }

    public Collection<Categoria> listarCategorias() {
        return categorias.findAll();
    }

    public Optional<Categoria> obtenerCategoriaPorNombre(String nombre) {
        return categorias.findByNombre(nombre);
    }

    public Optional<Categoria> obtenerCategoriaPorId(int id) {
        return categorias.findById(id);
    }

    
    public Transaccion registrarIngreso(BigDecimal monto, LocalDate fecha, String descripcion, int categoriaId) {
        validarMonto(monto);
        validarFecha(fecha);
        Categoria categoria = categorias.findById(categoriaId).orElse(null);
        Transaccion ing = new Ingreso(0, monto, fecha, descripcion, categoria);
        return transacciones.add(ing);
    }

    public Transaccion registrarGasto(BigDecimal monto, LocalDate fecha, String descripcion, int categoriaId) {
        validarMonto(monto);
        validarFecha(fecha);
        Categoria categoria = categorias.findById(categoriaId).orElse(null);
        Transaccion gas = new Gasto(0, monto, fecha, descripcion, categoria);
        return transacciones.add(gas);
    }

    public boolean editarTransaccion(int id, BigDecimal monto, LocalDate fecha, String descripcion, Integer categoriaId) {
        Optional<Transaccion> opt = transacciones.findById(id);
        if (!opt.isPresent()) return false;
        Transaccion t = opt.get();
        if (monto != null) validarMonto(monto);
        if (fecha != null) validarFecha(fecha);
        if (monto != null) t.setMonto(monto);
        if (fecha != null) t.setFecha(fecha);
        if (descripcion != null) t.setDescripcion(descripcion);
        if (categoriaId != null) {
            Categoria c = categorias.findById(categoriaId).orElse(null);
            t.setCategoria(c);
        }
        return transacciones.update(t);
    }

    public boolean eliminarTransaccion(int id) {
        return transacciones.delete(id);
    }

    public Collection<Transaccion> listarTransacciones() {
        return transacciones.findAll();
    }

    public List<Transaccion> filtrarPorCategoria(int categoriaId) {
        return transacciones.findByCategoriaId(categoriaId);
    }

    public List<Transaccion> buscarPorTexto(String texto) {
        String q = texto == null ? "" : texto.trim().toLowerCase();
        return transacciones.search(t ->
            (t.getDescripcion() != null && t.getDescripcion().toLowerCase().contains(q)) ||
            (t.getCategoria() != null && t.getCategoria().getNombre().toLowerCase().contains(q))
        );
    }

    
    public Map<String, Object> reporteMensual(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();
        return reporteEntreFechas(start, end);
    }

    public Map<String, Object> reporteSemanal(LocalDate anyDateInWeek) {
        
        LocalDate monday = anyDateInWeek.minusDays((anyDateInWeek.getDayOfWeek().getValue() + 6) % 7);
        LocalDate sunday = monday.plusDays(6);
        return reporteEntreFechas(monday, sunday);
    }

    public Map<String, Object> reporteEntreFechas(LocalDate start, LocalDate end) {
        List<Transaccion> lista = transacciones.findByFechaBetween(start, end);
        BigDecimal ingresos = suma(lista, TipoTransaccion.INGRESO);
        BigDecimal egresos = suma(lista, TipoTransaccion.GASTO);
        BigDecimal balance = ingresos.subtract(egresos);

        
        Map<String, BigDecimal> gastosPorCat = lista.stream()
                .filter(t -> t.getTipo() == TipoTransaccion.GASTO)
                .collect(Collectors.groupingBy(
                        t -> t.getCategoria() != null ? t.getCategoria().getNombre() : "(sin categoría)",
                        LinkedHashMap::new,
                        Collectors.mapping(Transaccion::getMonto,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        Map<String, Double> porcentajePorCat = new LinkedHashMap<>();
        if (egresos.compareTo(BigDecimal.ZERO) > 0) {
            for (Map.Entry<String, BigDecimal> e : gastosPorCat.entrySet()) {
                double pct = e.getValue().divide(egresos, 6, java.math.RoundingMode.HALF_UP).doubleValue() * 100.0;
                porcentajePorCat.put(e.getKey(), pct);
            }
        }

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("desde", start);
        out.put("hasta", end);
        out.put("ingresos", ingresos);
        out.put("egresos", egresos);
        out.put("balance", balance);
        out.put("gastosPorCategoria", gastosPorCat);
        out.put("porcentajeGastosPorCategoria", porcentajePorCat);
        out.put("transacciones", lista);
        return out;
    }

    private BigDecimal suma(List<Transaccion> lista, TipoTransaccion tipo) {
        return lista.stream()
                .filter(t -> t.getTipo() == tipo)
                .map(Transaccion::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validarMonto(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Monto inválido: debe ser un número positivo.");
        }
    }

    private void validarFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("Fecha inválida.");
        }
        
    }
}
