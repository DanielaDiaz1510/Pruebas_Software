package edu.compensar.ps2.app;

import edu.compensar.ps2.model.Categoria;
import edu.compensar.ps2.model.Transaccion;
import edu.compensar.ps2.repo.InMemoryCategoriaRepository;
import edu.compensar.ps2.repo.InMemoryTransaccionRepository;
import edu.compensar.ps2.service.FinanzasService;
import edu.compensar.ps2.report.Reporte;
import edu.compensar.ps2.util.InputUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FinanzasApp {

    private final FinanzasService service;
    private final Scanner sc = new Scanner(System.in);

    public FinanzasApp() {
        this.service = new FinanzasService(new InMemoryCategoriaRepository(), new InMemoryTransaccionRepository());
        seedCategorias();
    }

    private void seedCategorias() {
        
        if (service.listarCategorias().isEmpty()) {
            service.crearCategoria("Comida", "Gastos de alimentación");
            service.crearCategoria("Transporte", "Buses, gasolina, etc.");
            service.crearCategoria("Salario", "Ingresos por nómina");
        }
    }

    public static void main(String[] args) {
        new FinanzasApp().run();
    }

    private void run() {
        System.out.println("=== Finanzas Personales (Consola) ===");
        boolean exit = false;
        while (!exit) {
            menuPrincipal();
            int op = InputUtils.leerEntero(sc);
            switch (op) {
                case 1: registrarIngreso(); break;
                case 2: registrarGasto(); break;
                case 3: editarTransaccion(); break;
                case 4: eliminarTransaccion(); break;
                case 5: listarTransacciones(); break;
                case 6: filtrarPorCategoria(); break;
                case 7: gestionarCategorias(); break;
                case 8: reporteMensual(); break;
                case 9: reporteSemanal(); break;
                case 10: buscarTransacciones(); break;
                case 0: exit = true; break;
                default: System.out.println("Opción inválida.");
            }
        }
        System.out.println("¡Hasta pronto!");
    }

    private void menuPrincipal() {
        System.out.println(" Seleccione una opción:");
        System.out.println(" 1) Registrar ingreso");
        System.out.println(" 2) Registrar gasto");
        System.out.println(" 3) Editar transacción");
        System.out.println(" 4) Eliminar transacción");
        System.out.println(" 5) Listar transacciones");
        System.out.println(" 6) Filtrar por categoría");
        System.out.println(" 7) Gestionar categorías");
        System.out.println(" 8) Reporte mensual");
        System.out.println(" 9) Reporte semanal");
        System.out.println("10) Buscar transacciones");
        System.out.println(" 0) Salir");
        System.out.print("Opción: ");
    }

    
    private void registrarIngreso() {
        System.out.println(" == Registrar Ingreso ==");
        System.out.print("Monto: ");
        BigDecimal monto = InputUtils.leerMonto(sc);
        System.out.print("Fecha (yyyy-MM-dd): ");
        LocalDate fecha = InputUtils.leerFecha(sc);
        System.out.print("Descripción: ");
        String desc = sc.nextLine().trim();
        int categoriaId = elegirCategoria();
        try {
            service.registrarIngreso(monto, fecha, desc, categoriaId);
            System.out.println("Ingreso registrado.");
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void registrarGasto() {
        System.out.println("== Registrar Gasto ==");
        System.out.print("Monto: ");
        BigDecimal monto = InputUtils.leerMonto(sc);
        System.out.print("Fecha (yyyy-MM-dd): ");
        LocalDate fecha = InputUtils.leerFecha(sc);
        System.out.print("Descripción: ");
        String desc = sc.nextLine().trim();
        int categoriaId = elegirCategoria();
        try {
            service.registrarGasto(monto, fecha, desc, categoriaId);
            System.out.println("Gasto registrado.");
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void editarTransaccion() {
        listarTransacciones();
        System.out.print(" ID de la transacción a editar: ");
        int id = InputUtils.leerEntero(sc);

        System.out.print("Nuevo monto (o deje vacío para no cambiar): ");
        String sMonto = sc.nextLine().trim();
        BigDecimal monto = null;
        if (!sMonto.isEmpty()) {
            try { monto = new BigDecimal(sMonto.replace(',', '.')); }
            catch (NumberFormatException e) { System.out.println("Monto inválido, se ignorará."); }
        }

        System.out.print("Nueva fecha yyyy-MM-dd (o vacío): ");
        String sFecha = sc.nextLine().trim();
        LocalDate fecha = null;
        if (!sFecha.isEmpty()) {
            try { fecha = LocalDate.parse(sFecha); }
            catch (Exception e) { System.out.println("Fecha inválida, se ignorará."); }
        }

        System.out.print("Nueva descripción (o vacío): ");
        String desc = sc.nextLine().trim();
        if (desc.isEmpty()) desc = null;

        System.out.print("¿Cambiar categoría? (s/n): ");
        String s = sc.nextLine().trim().toLowerCase();
        Integer categoriaId = null;
        if (s.equals("s") || s.equals("si") || s.equals("sí")) {
            categoriaId = elegirCategoria();
        }

        boolean ok = service.editarTransaccion(id, monto, fecha, desc, categoriaId);
        System.out.println(ok ? "Transacción actualizada." : "No se encontró la transacción.");
    }

    private void eliminarTransaccion() {
        listarTransacciones();
        System.out.print(" ID de la transacción a eliminar: ");
        int id = InputUtils.leerEntero(sc);
        if (InputUtils.confirmar(sc, "¿Confirma eliminar esta transacción?")) {
            boolean ok = service.eliminarTransaccion(id);
            System.out.println(ok ? "Transacción eliminada." : "No se encontró la transacción.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private void listarTransacciones() {
        System.out.println(" == Todas las transacciones ==");
        for (Transaccion t : service.listarTransacciones()) {
            System.out.println("  " + t);
        }
    }

    private void filtrarPorCategoria() {
        int categoriaId = elegirCategoria();
        List<Transaccion> lista = service.filtrarPorCategoria(categoriaId);
        System.out.println(" == Transacciones en la categoría seleccionada ==");
        for (Transaccion t : lista) {
            System.out.println("  " + t);
        }
    }

    private void buscarTransacciones() {
        System.out.print("Texto a buscar (en descripción o nombre de categoría): ");
        String q = sc.nextLine().trim();
        List<Transaccion> lista = service.buscarPorTexto(q);
        if (lista.isEmpty()) {
            System.out.println("  (sin resultados)");
        } else {
            System.out.println("== Resultados de búsqueda ==");
            for (Transaccion t : lista) {
                System.out.println("  " + t);
            }
        }
    }

    
    private void gestionarCategorias() {
        boolean back = false;
        while (!back) {
            System.out.println("== Categorías ==");
            System.out.println(" 1) Listar");
            System.out.println(" 2) Crear");
            System.out.println(" 3) Editar");
            System.out.println(" 4) Eliminar");
            System.out.println(" 0) Volver");
            System.out.print("Opción: ");
            int op = InputUtils.leerEntero(sc);
            switch (op) {
                case 1: listarCategorias(); break;
                case 2: crearCategoria(); break;
                case 3: editarCategoria(); break;
                case 4: eliminarCategoria(); break;
                case 0: back = true; break;
                default: System.out.println("Opción inválida.");
            }
        }
    }

    private void listarCategorias() {
        System.out.println("== Categorías ==");
        for (Categoria c : service.listarCategorias()) {
            System.out.println("  " + c);
        }
    }

    private void crearCategoria() {
        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();
        System.out.print("Descripción: ");
        String desc = sc.nextLine().trim();
        Categoria c = service.crearCategoria(nombre, desc);
        System.out.println("Creada: " + c);
    }

    private void editarCategoria() {
        listarCategorias();
        System.out.print("ID a editar: ");
        int id = InputUtils.leerEntero(sc);
        System.out.print("Nuevo nombre: ");
        String nombre = sc.nextLine().trim();
        System.out.print("Nueva descripción: ");
        String desc = sc.nextLine().trim();
        boolean ok = service.editarCategoria(id, nombre, desc);
        System.out.println(ok ? "Categoría actualizada." : "No encontrada.");
    }

    private void eliminarCategoria() {
        listarCategorias();
        System.out.print("ID a eliminar: ");
        int id = InputUtils.leerEntero(sc);
        if (InputUtils.confirmar(sc, "¿Confirma eliminar la categoría?")) {
            boolean ok = service.eliminarCategoria(id);
            System.out.println(ok ? "Categoría eliminada." : "No encontrada.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    
    private void reporteMensual() {
        System.out.print("Año (e.g., 2025): ");
        int y = InputUtils.leerEntero(sc);
        System.out.print("Mes (1-12): ");
        int m = InputUtils.leerEntero(sc);
        Map<String, Object> datos = service.reporteMensual(y, m);
        System.out.println(Reporte.formatear(datos));
    }

    private void reporteSemanal() {
        System.out.print("Una fecha dentro de la semana (yyyy-MM-dd): ");
        LocalDate d = InputUtils.leerFecha(sc);
        Map<String, Object> datos = service.reporteSemanal(d);
        System.out.println(Reporte.formatear(datos));
    }

    
    private int elegirCategoria() {
        while (true) {
            System.out.println("Elija categoría por ID:");
            for (Categoria c : service.listarCategorias()) {
                System.out.println("  " + c);
            }
            System.out.print("ID: ");
            int id = InputUtils.leerEntero(sc);
            if (service.obtenerCategoriaPorId(id).isPresent()) return id;
            System.out.println("ID inválido. Intente nuevamente.");
        }
    }
}
