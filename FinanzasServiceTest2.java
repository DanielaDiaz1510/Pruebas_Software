package edu.compensar.ps2.service;

import edu.compensar.ps2.model.Categoria;
import edu.compensar.ps2.model.Transaccion;
import edu.compensar.ps2.repo.InMemoryCategoriaRepository;
import edu.compensar.ps2.repo.InMemoryTransaccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FinanzasServiceTest2 {

    FinanzasService service;
    Categoria catComida;
    Categoria catTransporte;

    @BeforeEach
    void setup() {
        service = new FinanzasService(new InMemoryCategoriaRepository(), new InMemoryTransaccionRepository());
        catComida = service.crearCategoria("Comida", "Alimentos");
        catTransporte = service.crearCategoria("Transporte", "Buses");
    }

    @Test
    void crudCategorias() {
        assertEquals(2, service.listarCategorias().size());
        assertTrue(service.obtenerCategoriaPorNombre("comida").isPresent());
        assertTrue(service.editarCategoria(catTransporte.getId(), "Movilidad", "Transporte público"));
        assertTrue(service.eliminarCategoria(catTransporte.getId()));
        assertFalse(service.obtenerCategoriaPorId(catTransporte.getId()).isPresent());
    }

    @Test
    void registrarYEditarTransaccionesYBuscar() {
        Transaccion g = service.registrarGasto(new BigDecimal("12.50"),
                LocalDate.of(2025,10,1), "pan y leche", catComida.getId());
        assertTrue(g.getId() > 0);

        boolean ok = service.editarTransaccion(g.getId(), new BigDecimal("13.00"),
                null, null, null);
        assertTrue(ok);

        List<Transaccion> lista = service.buscarPorTexto("leche");
        assertEquals(1, lista.size());

        assertTrue(service.eliminarTransaccion(g.getId()));
        assertEquals(0, service.listarTransacciones().size());
    }

    @Test
    void validarMontosYFechas() {
        assertThrows(IllegalArgumentException.class, () ->
                service.registrarIngreso(new BigDecimal("-1"), LocalDate.now(), "x", catComida.getId()));
        assertThrows(IllegalArgumentException.class, () ->
                service.registrarGasto(BigDecimal.ZERO, LocalDate.now(), "x", catComida.getId()));
        assertThrows(IllegalArgumentException.class, () ->
                service.registrarIngreso(BigDecimal.ONE, null, "x", catComida.getId()));
    }

    @Test
    void reportesMensualSemanalYEntreFechas() {
        service.registrarIngreso(BigDecimal.valueOf(1000L), LocalDate.of(2025,10,2), "salario", 0);
        service.registrarGasto(BigDecimal.valueOf(100L), LocalDate.of(2025,10,3), "comida", catComida.getId());
        service.registrarGasto(BigDecimal.valueOf(150L), LocalDate.of(2025,10,4), "bus", catTransporte.getId());

        Map<String,Object> repMes = service.reporteMensual(2025, 10);
        assertEquals(BigDecimal.valueOf(1000L), repMes.get("ingresos"));
        assertEquals(BigDecimal.valueOf(250L), repMes.get("egresos"));
        assertEquals(BigDecimal.valueOf(750L), repMes.get("balance"));

        Map<String,Object> repSemana = service.reporteSemanal(LocalDate.of(2025,10,3));
        assertNotNull(repSemana.get("desde"));
        assertNotNull(repSemana.get("hasta"));

        Map<String, Double> pct = (Map<String, Double>) repMes.get("porcentajeGastosPorCategoria");
        assertEquals(2, pct.size());
        double sum = pct.values().stream().mapToDouble(Double::doubleValue).sum();
        assertTrue(sum > 99.9 && sum < 100.1);
    }


}
