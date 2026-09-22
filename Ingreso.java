package edu.compensar.ps2.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Ingreso extends Transaccion {
    public Ingreso(int id, BigDecimal monto, LocalDate fecha, String descripcion, Categoria categoria) {
        super(id, monto, fecha, descripcion, categoria, TipoTransaccion.INGRESO);
    }
}
