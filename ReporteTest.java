package edu.compensar.ps2.report;

import edu.compensar.ps2.model.Categoria;
import edu.compensar.ps2.repo.InMemoryCategoriaRepository;
import edu.compensar.ps2.repo.InMemoryTransaccionRepository;
import edu.compensar.ps2.service.FinanzasService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReporteTest {

    @Test
    void formateoIncluyeCamposClaves() {
        FinanzasService service = new FinanzasService(
                new InMemoryCategoriaRepository(),
                new InMemoryTransaccionRepository()
        );
        Categoria c = service.crearCategoria("Varios", "varios");
        service.registrarIngreso(BigDecimal.valueOf(500L), LocalDate.of(2025,10,1), "salario", c.getId());
        service.registrarGasto(BigDecimal.valueOf(200L), LocalDate.of(2025,10,2), "compra", c.getId());

        Map<String,Object> datos = service.reporteEntreFechas(LocalDate.of(2025,10,1), LocalDate.of(2025,10,31));
        String s = Reporte.formatear(datos);
        assertTrue(s.contains("REPORTE"));
        assertTrue(s.contains("Ingresos"));
        assertTrue(s.contains("Egresos"));
        assertTrue(s.contains("Balance"));
        assertTrue(s.contains("Transacciones del periodo"));
    }

}
