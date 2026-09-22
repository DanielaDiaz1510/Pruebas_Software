package edu.compensar.ps2.app;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class FinanzasAppTest {

    private PrintStream originalOut;
    private InputStream originalIn;
    private ByteArrayOutputStream baos;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        originalIn = System.in;
        baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void flujoCompletoUsuarioPorConsola() throws Exception {
        StringBuilder script = new StringBuilder();

        
        script.append("1\n");
        script.append("1000\n");              
        script.append("2025-10-10\n");        
        script.append("salario\n");           
        script.append("3\n");                 

        
        script.append("2\n");
        script.append("100\n");               
        script.append("2025-10-11\n");        
        script.append("mercado pan\n");       
        script.append("1\n");                 

        
        script.append("5\n");

        
        script.append("3\n");
        script.append("2\n");                 
        script.append("150\n");               
        script.append("\n");                  
        script.append("ajuste\n");            
        script.append("n\n");                 

        
        script.append("8\n");
        script.append("2025\n");
        script.append("10\n");

        
        script.append("6\n");
        script.append("1\n");

        
        script.append("10\n");
        script.append("ajuste\n");

        
        script.append("4\n");
        script.append("2\n");
        script.append("s\n");                 

        
        script.append("7\n");
        script.append("2\n");                 
        script.append("Ahorro\n");
        script.append("Meta\n");
        script.append("3\n");                 
        script.append("4\n");                 
        script.append("Ahorros\n");
        script.append("Metas\n");
        script.append("4\n");                 
        script.append("4\n");                 
        script.append("s\n");                 
        script.append("0\n");                 

        
        script.append("9\n");
        script.append("2025-10-11\n");

        
        script.append("0\n");

        System.setIn(new ByteArrayInputStream(script.toString().getBytes(StandardCharsets.UTF_8)));

        
        FinanzasApp.main(new String[0]);

        String out = baos.toString(StandardCharsets.UTF_8);
        
        assertTrue(out.contains("Ingreso registrado."), "Debe registrar ingreso");
        assertTrue(out.contains("Gasto registrado."), "Debe registrar gasto");
        assertTrue(out.contains("Todas las transacciones"), "Debe listar transacciones");
        assertTrue(out.contains("Transacción actualizada."), "Debe editar transacción");
        assertTrue(out.contains("Ingresos:") && out.contains("Egresos") && out.contains("Balance"),
                "El reporte debe mostrar ingresos/egresos/balance");
        assertTrue(out.contains("Transacciones en la categoría"), "Debe filtrar por categoría");
        assertTrue(out.toLowerCase().contains("resultados de búsqueda"), "Debe mostrar resultados de búsqueda");
        assertTrue(out.contains("Transacción eliminada."), "Debe eliminar transacción");
        assertTrue(out.contains("Creada:") && out.contains("Ahorro"), "Debe crear categoría");
        assertTrue(out.contains("Categoría actualizada."), "Debe editar categoría");
        assertTrue(out.contains("Categoría eliminada."), "Debe eliminar categoría");
        assertTrue(out.contains("¡Hasta pronto!"), "Debe salir");
    }

}
