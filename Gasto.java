package edu.compensar.ps2.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Gasto extends Transaccion {
    public Gasto(int id, BigDecimal monto, LocalDate fecha, String descripcion, Categoria categoria) {
        super(id, monto, fecha, descripcion, categoria, TipoTransaccion.GASTO);
    }
}
