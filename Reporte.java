package edu.compensar.ps2.report;

import edu.compensar.ps2.model.Transaccion;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Reporte {

    public static String formatear(Map<String, Object> datos) {
        StringBuilder sb = new StringBuilder();
        LocalDate desde = (LocalDate) datos.get("desde");
        LocalDate hasta = (LocalDate) datos.get("hasta");
        BigDecimal ingresos = (BigDecimal) datos.get("ingresos");
        BigDecimal egresos = (BigDecimal) datos.get("egresos");
        BigDecimal balance = (BigDecimal) datos.get("balance");
        Map<String, BigDecimal> gastosPorCat = (Map<String, BigDecimal>) datos.get("gastosPorCategoria");
        Map<String, Double> pctPorCat = (Map<String, Double>) datos.get("porcentajeGastosPorCategoria");
        List<Transaccion> trans = (List<Transaccion>) datos.get("transacciones");

        sb.append(" ================ REPORTE ================\n");
        sb.append("Periodo: ").append(desde).append(" a ").append(hasta).append(" \n");
        sb.append("Ingresos: ").append(ingresos.toPlainString()).append(" \n");
        sb.append("Egresos : ").append(egresos.toPlainString()).append(" \n");
        sb.append("Balance : ").append(balance.toPlainString()).append(" \n");

        sb.append("Gastos por categoría: ");
        if (gastosPorCat.isEmpty()) {
            sb.append("(sin gastos registrados) \n");
        } else {
            for (Map.Entry<String, BigDecimal> e : gastosPorCat.entrySet()) {
                double pct = pctPorCat.getOrDefault(e.getKey(), 0.0);
                sb.append(String.format("  - %s: %s (%.2f%%) ", e.getKey(), e.getValue().toPlainString(), pct));
            }
        }

        sb.append("Transacciones del periodo: ");
        if (trans.isEmpty()) {
            sb.append("(ninguna) \n");
        } else {
            for (Transaccion t : trans) {
                sb.append("  ").append(t).append(" ");
            }
        }

        sb.append("========================================= ");
        return sb.toString();
    }
}
