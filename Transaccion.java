package edu.compensar.ps2.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public abstract class Transaccion {
    private int id; 
    private BigDecimal monto;
    private LocalDate fecha;
    private String descripcion;
    private Categoria categoria;
    private final TipoTransaccion tipo;

    protected Transaccion(int id, BigDecimal monto, LocalDate fecha, String descripcion, Categoria categoria, TipoTransaccion tipo) {
        this.id = id;
        this.monto = monto;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.tipo = tipo;
    }

    public int getId() { return id; }
    public BigDecimal getMonto() { return monto; }
    public LocalDate getFecha() { return fecha; }
    public String getDescripcion() { return descripcion; }
    public Categoria getCategoria() { return categoria; }
    public TipoTransaccion getTipo() { return tipo; }

    
    public void setId(int id) { this.id = id; }

    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    @Override
    public String toString() {
        return String.format("[%d] %s %s | %s | %s | %s",
                id,
                tipo == TipoTransaccion.INGRESO ? "+" : "-",
                monto.toPlainString(),
                fecha.toString(),
                categoria != null ? categoria.getNombre() : "(sin categoría)",
                descripcion == null ? "" : descripcion);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaccion)) return false;
        Transaccion that = (Transaccion) o;
        return id == that.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
