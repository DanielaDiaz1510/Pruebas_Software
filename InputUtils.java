package edu.compensar.ps2.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputUtils {
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static BigDecimal leerMonto(Scanner sc) {
        while (true) {
            String s = sc.nextLine().trim().replace(',', '.');
            try {
                BigDecimal val = new BigDecimal(s);
                if (val.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Monto inválido (debe ser > 0). Intente de nuevo:");
                    continue;
                }
                return val;
            } catch (NumberFormatException ex) {
                System.out.println("Entrada inválida. Escriba un número, por ejemplo 1234.56");
            }
        }
    }

    public static LocalDate leerFecha(Scanner sc) {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                return LocalDate.parse(s, DATE_FMT);
            } catch (DateTimeParseException ex) {
                System.out.println("Fecha inválida. Formato esperado: yyyy-MM-dd (ej: 2025-10-11). Intente de nuevo:");
            }
        }
    }

    public static int leerEntero(Scanner sc) {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException ex) {
                System.out.println("Entrada inválida. Escriba un número entero:");
            }
        }
    }

    public static boolean confirmar(Scanner sc, String pregunta) {
        System.out.print(pregunta + " (s/n): ");
        while (true) {
            String s = sc.nextLine().trim().toLowerCase();
            if (s.equals("s") || s.equals("si") || s.equals("sí")) return true;
            if (s.equals("n") || s.equals("no")) return false;
            System.out.print("Responda 's' o 'n': ");
        }
    }
}
